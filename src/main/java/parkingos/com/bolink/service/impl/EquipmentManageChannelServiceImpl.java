package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.mybatis.mapper.CameraTbMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CameraTb;
import parkingos.com.bolink.models.ComPassTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.EquipmentManageChannelService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.redis.RedisService;
import parkingos.com.bolink.utils.CommonUtils;

import java.util.Map;

@Service
public class EquipmentManageChannelServiceImpl implements EquipmentManageChannelService {

    Logger logger = LoggerFactory.getLogger(EquipmentManageChannelServiceImpl.class);


    @Autowired
    private SupperSearchService<ComPassTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private RedisService redisService;
    @Autowired
    CameraTbMapper cameraTbMapper;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        Long comid = Long.valueOf(Integer.valueOf(reqmap.get("comid")));
        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setState(0);
        comPassTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(comPassTb,reqmap);

        return result;
    }

    @Override
    @Transactional
    public Integer insertResultByConditions(ComPassTb comPassTb) {
        Integer result = commonDao.insert(comPassTb);
        if(result==1){
            commonUtils.sendMessage(comPassTb,comPassTb.getComid(),comPassTb.getId(),1);

            SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
            syncInfoPoolTb.setOperate(0);
            syncInfoPoolTb.setComid(comPassTb.getComid());
            syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
            syncInfoPoolTb.setTableId(comPassTb.getId());
            syncInfoPoolTb.setTableName("com_pass_tb");
            syncInfoPoolTb.setState(0);
            syncInfoPoolTb.setUpdateTime(System.currentTimeMillis()/1000);
            int ins = commonDao.insert(syncInfoPoolTb);
            logger.info("插入同步表:"+ins);
        }
        return result;
    }

    @Override
    @Transactional
    public Integer updateResultByConditions(ComPassTb comPassTb) {
        Integer result = commonDao.updateByPrimaryKey(comPassTb);
        if(result==1){
            ComPassTb con = new ComPassTb();
            con.setId(comPassTb.getId());
            comPassTb=(ComPassTb)commonDao.selectObjectByConditions(con);

            commonUtils.sendMessage(comPassTb,comPassTb.getComid(),comPassTb.getId(),2);

            SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
            syncInfoPoolTb.setOperate(1);
            syncInfoPoolTb.setComid(comPassTb.getComid());
            syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
            syncInfoPoolTb.setTableId(comPassTb.getId());
            syncInfoPoolTb.setTableName("com_pass_tb");
            syncInfoPoolTb.setState(0);
            syncInfoPoolTb.setUpdateTime(System.currentTimeMillis()/1000);
            int ins = commonDao.insert(syncInfoPoolTb);
            logger.info("插入同步表:"+ins);
        }
        return result;
    }

    @Override
    @Transactional
    public Integer removeResultByConditions(ComPassTb comPassTb) {
        Integer result = commonDao.updateByPrimaryKey(comPassTb);
        if(result==1){
            ComPassTb con = new ComPassTb();
            con.setId(comPassTb.getId());
            comPassTb=(ComPassTb)commonDao.selectObjectByConditions(con);

            commonUtils.sendMessage(comPassTb,comPassTb.getComid(),comPassTb.getId(),3);
            SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
            syncInfoPoolTb.setOperate(2);
            syncInfoPoolTb.setComid(comPassTb.getComid());
            syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
            syncInfoPoolTb.setTableId(comPassTb.getId());
            syncInfoPoolTb.setTableName("com_pass_tb");
            syncInfoPoolTb.setState(0);
            syncInfoPoolTb.setUpdateTime(System.currentTimeMillis()/1000);
            int ins = commonDao.insert(syncInfoPoolTb);
            logger.info("插入同步表:"+ins);
        }
        return result;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(ComPassTb.class);
    }

    @Override
    @Transactional
    public String addChannel(String passname, String passtype, Integer monthSet, String description, Long comid, Long cameraId) {
        Long id= getId();
        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setId(id);
        comPassTb.setPassname(passname);
        comPassTb.setPasstype(passtype);
        comPassTb.setMonthSet(monthSet);
        comPassTb.setDescription(description);
        comPassTb.setComid(comid);
        comPassTb.setChannelId(id+"");
        comPassTb.setState(0);
        if(cameraId>0) {
            comPassTb.setCameraId(cameraId);
        }
        int insertCount = insertResultByConditions(comPassTb);

        //双向同步  相机表 也要保存通道信息
        if(cameraId>0){
            CameraTb cameraTb = new CameraTb();
            cameraTb.setId(cameraId);
            cameraTb.setChannelId(id);
            commonDao.updateByPrimaryKey(cameraTb);

            cameraTb = cameraTbMapper.selectByPrimaryKey(cameraId);
            String camId = cameraTb.getCamId();
            redisService.set("camera_"+camId,cameraTb);
        }
        return insertCount+"";
    }

    @Override
    @Transactional
    public String updateChannel(Long id, String passname, String passtype, Integer monthSet, String description, Long cameraId) {


        if(cameraId>0) {
            //把相机里面是这个通道的更新掉
            CameraTb cameraTb = new CameraTb();
            cameraTb.setChannelId(id);
            CameraTb update = new CameraTb();
            update.setChannelId(-1L);
            commonDao.updateByConditions(update, cameraTb);

            //把之前绑定这个相机的 通道 的相机更新掉。
            ComPassTb con = new ComPassTb();
            con.setCameraId(cameraId);
            ComPassTb updateCom = new ComPassTb();
            updateCom.setCameraId(-1L);
            commonDao.updateByConditions(updateCom,con);
        }




        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setId(id);
        comPassTb.setPassname(passname);
        comPassTb.setPasstype(passtype);
        comPassTb.setMonthSet(monthSet);
        comPassTb.setDescription(description);
        if(cameraId>0) {
            comPassTb.setCameraId(cameraId);
        }
        int updatePass = commonDao.updateByPrimaryKey(comPassTb);

        //把相机绑定现在的通道
        if(cameraId>0) {
            CameraTb cameraTb = new CameraTb();
            cameraTb.setId(cameraId);
            cameraTb.setChannelId(id);
            commonDao.updateByPrimaryKey(cameraTb);

            cameraTb = cameraTbMapper.selectByPrimaryKey(cameraId);
            String camId = cameraTb.getCamId();
            redisService.set("camera_"+camId,cameraTb);
        }


        return updatePass+"";

    }

}
