package parkingos.com.bolink.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.GetDataService;

@Service
public class GetDataServiceImpl implements GetDataService {

    Logger logger = Logger.getLogger(GetDataServiceImpl.class);

    @Autowired
    private CommonDao commonDao;


    @Override
    public String  getNicknameById(Long id) {
        String result = "";
        if(id>0) {
            UserInfoTb userInfoTb = new UserInfoTb();
            userInfoTb.setId(id);
            userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
            if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
                result = userInfoTb.getNickname();
            }
        }
        return result;
    }

    @Override
    public String getCarTypeById(Long id) {
        String result = "";
        if(id>0) {
            CarTypeTb carTypeTb = new CarTypeTb();
            carTypeTb.setId(id);
            carTypeTb = (CarTypeTb) commonDao.selectObjectByConditions(carTypeTb);
            if(carTypeTb!=null&&carTypeTb.getName()!=null){
                result = carTypeTb.getName();
            }
        }
        return result;
    }
}
