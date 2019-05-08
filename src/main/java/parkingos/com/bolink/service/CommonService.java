package parkingos.com.bolink.service;


import parkingos.com.bolink.models.ComInfoTb;

public interface CommonService {


    Long getUnionIdByComid(Long parkId);

    String getUkeyByUnionId(Long unionId);

    String getBolinkIdByComid(Long parkId);

    Long getCityIdByComid(Long parkId);

    String getTableNameByComid(Long comid,int type);

    String getUserNameById(Long userId);

    String getTableNameByGroupId(Long groupid, int i);

    String getParkNameById(Long comId);

//    Long getParkIdByBolinkId(String comid);

    Long getGroupIdByComid(Long parkId);

    String getComName(long comid);

    ComInfoTb getComInfoByUnionIdAndParkId(String unionId, String comid);

    ComInfoTb getComInfoByComid(Long comid);

    void deleteCachPark(Long id,String unionId, String bolinkid);

    int getParkEmpty(int comid);
}
