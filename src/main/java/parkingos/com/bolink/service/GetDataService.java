package parkingos.com.bolink.service;

public interface GetDataService {

    String getNicknameById(Long id);

    String getCarType(Long comid, Long groupid);

    String getprodsum(Long prodId, Integer months);

    String getpname(Long comid);

    String getalluser(Long comid, Long groupid);

    String getMonitorName(String comid);

    String getChannelType(String comid);

    String getWorkSiteId(String comid);

    String getAllParks(String groupid, String cityid);

    String getAllCollectors(String groupid, String cityid);

    String getAllPackage(String groupid,String cityid);

    String getAllUnion(Long cityid);

    String getSuperimposed(String comid);

    String getGroupChannelTypes(Long groupid);
}
