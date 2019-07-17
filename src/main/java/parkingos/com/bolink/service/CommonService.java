package parkingos.com.bolink.service;


import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.models.OrgGroupTb;

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

    void deleteCachPark(String unionId, String bolinkid);

    int getParkEmpty(int comid);

    OrgCityMerchants getCityByUnionId(String unionId);

    OrgGroupTb getOrgGroupById(Long groupId);

    OrgCityMerchants getOrgCityById(Long union_id);

    void deleteCachOrgGroup(Long groupid);

    void deleteCachCity(Long id);

    OrgCityMerchants reGetOrgCityById(Long cityid);
}
