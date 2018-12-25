package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PrepayCardTb;
import parkingos.com.bolink.models.PrepayCardTrade;
import parkingos.com.bolink.service.PrepayCardService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.CommonUtils;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.TimeTools;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;


@Service
public class PrepayCardServiceImpl implements PrepayCardService {

    Logger logger = LoggerFactory.getLogger(PrepayCardServiceImpl.class);

    @Autowired
    SupperSearchService<PrepayCardTb> supperSearchService;
    @Autowired
    CommonDao commonDao;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqParameterMap) {
        Long comid = Long.parseLong(reqParameterMap.get("comid"));
        PrepayCardTb prepayCardTb = new PrepayCardTb();
        prepayCardTb.setParkId(comid);
        prepayCardTb.setState(0);
        JSONObject result = supperSearchService.supperSearch(prepayCardTb,reqParameterMap);
        return result;
    }

    @Override
    public JSONObject renewProduct(HttpServletRequest req) {
        JSONObject result = new JSONObject();
        result.put("state",0);
        Long id = RequestUtil.getLong(req,"id",-1L);
        Long comid = RequestUtil.getLong(req, "comid", -1L);
        String addMoney = RequestUtil.getString(req,"add_money");
        String remark = RequestUtil.getString(req,"remark");
        logger.info("===>>>"+id+"~~"+comid+"~~~"+addMoney+"~~"+remark);
        if(!Check.isDouble(addMoney)){
            result.put("msg","续费金额错误！");
            return result;
        }

        PrepayCardTb prepayCardTb = new PrepayCardTb();
        prepayCardTb.setId(id);

        prepayCardTb=(PrepayCardTb)commonDao.selectObjectByConditions(prepayCardTb);
        if(prepayCardTb==null){
            result.put("msg","月卡查询失败！");
            return result;
        }

        Double money = prepayCardTb.getMoney().doubleValue();
        Double afterMoney=money+Double.parseDouble(addMoney);
        prepayCardTb.setMoney(new BigDecimal(afterMoney));
        prepayCardTb.setRemark(remark);
        int update = commonDao.updateByPrimaryKey(prepayCardTb);

        if(update==1){
            result.put("state",1);
            result.put("msg","续费成功！");
            commonUtils.sendMessage(prepayCardTb,comid,id,2);
            //写入同步表
            int insertSync = commonUtils.insertSync(prepayCardTb,1,comid,id);

            //充值成功记流水，同样写入流水表
            PrepayCardTrade trade = new PrepayCardTrade();
            Long tradeId = commonDao.selectSequence(PrepayCardTrade.class);
            String tradeNo = TimeTools.getTimeYYYYMMDDHHMMSS() + "" + comid;
            trade.setName(prepayCardTb.getName());
            trade.setId(tradeId);
            trade.setTradeNo(tradeNo);
            trade.setAddMoneyBefore(new BigDecimal(money));
            trade.setAddMoney(new BigDecimal(addMoney));
            trade.setAddMoneyAfter(new BigDecimal(afterMoney));
            trade.setCardId(prepayCardTb.getCardId());
            trade.setCarNumber(prepayCardTb.getCarNumber());
            trade.setCreateTime(System.currentTimeMillis()/1000);
            trade.setMobile(prepayCardTb.getMobile());
            trade.setParkId(comid);
            trade.setPayTime(System.currentTimeMillis()/1000);
            trade.setRemark(remark);
            trade.setTradeType("云平台续费");
            trade.setPayType("现金");//现金
            int insert = commonDao.insert(trade);

            commonUtils.sendMessage(trade,comid,tradeId,1);
            int sync = commonUtils.insertSync(trade,0,comid,tradeId);
        }
        return result;
    }

    @Override
    public JSONObject createPrepayCard(HttpServletRequest req) {

        JSONObject result = new JSONObject();
        result.put("state",0);
        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String name = RequestUtil.getString(req,"name");
        String mobile = RequestUtil.getString(req,"mobile");
        String carNumber = RequestUtil.getString(req,"car_number");
        String remark = RequestUtil.getString(req,"remark");

        Long id = commonDao.selectSequence(PrepayCardTb.class);

        PrepayCardTb prepayCardTb = new PrepayCardTb();
        prepayCardTb.setId(id);
        prepayCardTb.setRemark(remark);
        prepayCardTb.setMoney(new BigDecimal(0.0));
        prepayCardTb.setState(0);
        prepayCardTb.setParkId(comid);
        prepayCardTb.setCardId(id+"");
        prepayCardTb.setCtime(System.currentTimeMillis()/1000);
        prepayCardTb.setMobile(mobile);
        prepayCardTb.setCarNumber(carNumber);
        prepayCardTb.setName(name);

        int insert = commonDao.insert(prepayCardTb);
        if(insert==1){

            commonUtils.sendMessage(prepayCardTb,comid,id,1);
            //插入同步表。。
            int insertSync = commonUtils.insertSync(prepayCardTb,0,comid,id);
            result.put("state",1);
            result.put("msg","新建储值卡成功！");
            result.put("id",id);

            //充值成功记流水，同样写入流水表
            PrepayCardTrade trade = new PrepayCardTrade();
            Long tradeId = commonDao.selectSequence(PrepayCardTrade.class);
            String tradeNo = TimeTools.getTimeYYYYMMDDHHMMSS() + "" + comid;
            trade.setId(tradeId);
            trade.setName(name);
            trade.setTradeNo(tradeNo);
            trade.setAddMoney(new BigDecimal(0.0));
            trade.setCardId(prepayCardTb.getCardId());
            trade.setCarNumber(prepayCardTb.getCarNumber());
            trade.setCreateTime(System.currentTimeMillis()/1000);
            trade.setMobile(prepayCardTb.getMobile());
            trade.setParkId(comid);
            trade.setPayTime(System.currentTimeMillis()/1000);
            trade.setRemark(remark);
            trade.setPayType("现金");
            trade.setTradeType("云平台续费");
            trade.setAddMoneyAfter(new BigDecimal(0.0));
            trade.setAddMoneyBefore(new BigDecimal(0.0));
            int insertTrde = commonDao.insert(trade);

            commonUtils.sendMessage(trade,comid,tradeId,1);
            int sync = commonUtils.insertSync(trade,0,comid,tradeId);

        }
        return result;
    }

    @Override
    public JSONObject editCard(HttpServletRequest req) {

        JSONObject result = new JSONObject();
        result.put("state",0);
        Long id = RequestUtil.getLong(req,"id",-1L);
        Long comid = RequestUtil.getLong(req,"comid",-1L);
        String name = RequestUtil.getString(req,"name");
        String mobile = RequestUtil.getString(req,"mobile");
        String carNumber = RequestUtil.getString(req,"car_number");
        String remark = RequestUtil.getString(req,"remark");

        PrepayCardTb prepayCardTb = new PrepayCardTb();
        prepayCardTb.setId(id);
        prepayCardTb.setRemark(remark);
        prepayCardTb.setMobile(mobile);
        prepayCardTb.setCarNumber(carNumber);
        prepayCardTb.setName(name);
        prepayCardTb.setUtime(System.currentTimeMillis()/1000);

        int upadte = commonDao.updateByPrimaryKey(prepayCardTb);
        if(upadte==1){

            commonUtils.sendMessage(prepayCardTb,comid,id,2);
            //插入同步表。。
            int insertSync = commonUtils.insertSync(prepayCardTb,1,comid,id);
            result.put("state",1);
            result.put("msg","编辑储值卡成功！");
        }
        return result;

    }

    @Override
    public JSONObject deleteCard(Long id, Long comid) {

        JSONObject result = new JSONObject();
        result.put("state",0);

        PrepayCardTb prepayCardTb = new PrepayCardTb();
        prepayCardTb.setId(id);
        prepayCardTb.setState(1);

        int upadte = commonDao.updateByPrimaryKey(prepayCardTb);
        if(upadte==1){
            commonUtils.sendMessage(prepayCardTb,comid,id,3);
            //插入同步表。。
            int insertSync = commonUtils.insertSync(prepayCardTb,2,comid,id);
            result.put("state",1);
            result.put("msg","删除储值卡成功！");
        }
        return result;
    }
}
