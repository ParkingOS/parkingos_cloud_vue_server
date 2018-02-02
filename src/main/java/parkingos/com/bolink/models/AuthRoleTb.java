package parkingos.com.bolink.models;

public class AuthRoleTb {
    private Long id;

    private Long authId;

    private Long roleId;

    private String subAuth;

    private Long creatorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getSubAuth() {
        return subAuth;
    }

    public void setSubAuth(String subAuth) {
        this.subAuth = subAuth == null ? null : subAuth.trim();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}