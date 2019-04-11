package parkingos.com.bolink.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.SaveLogService;

@Service
public class SavaLogServiceImpl implements SaveLogService {

    Logger logger = LoggerFactory.getLogger(SavaLogServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
@Autowired
    CommonService commonService;

    @Override
    public void saveLog(ParkLogTb parkLogTb) {
        if(parkLogTb.getParkId()!=null&&parkLogTb.getParkId()>0){
            Long groupId=commonService.getGroupIdByComid(parkLogTb.getParkId());
            parkLogTb.setGroupId(groupId);
        }
        commonDao.insert(parkLogTb);
    }
}
