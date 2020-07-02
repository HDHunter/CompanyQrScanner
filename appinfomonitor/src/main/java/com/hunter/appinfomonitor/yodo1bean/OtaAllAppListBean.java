package com.hunter.appinfomonitor.yodo1bean;

import java.io.Serializable;
import java.util.List;

public class OtaAllAppListBean implements Serializable {

    private boolean success;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private List<TeamsBean> teams;

        public List<TeamsBean> getTeams() {
            return teams;
        }

        public void setTeams(List<TeamsBean> teams) {
            this.teams = teams;
        }

        public static class TeamsBean implements Serializable {

            private String _id;
            private String name;
            private String role;
            private String allPackageTags;
            private List<AppsBean> apps;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getAllPackageTags() {
                return allPackageTags;
            }

            public void setAllPackageTags(String allPackageTags) {
                this.allPackageTags = allPackageTags;
            }

            public List<AppsBean> getApps() {
                return apps;
            }

            public void setApps(List<AppsBean> apps) {
                this.apps = apps;
            }

            public static class AppsBean implements Serializable {
                private TodayDownloadCountBean todayDownloadCount;
                private GrayStrategyBean grayStrategy;
                private boolean isInTrash;
                private boolean autoPublish;
                private String updateMode;
                private int totalDownloadCount;
                private String _id;
                private String appName;
                private String bundleId;
                private String platform;
                private String creator;
                private String creatorId;
                private String icon;
                private String shortUrl;
                private String createAt;
                private String ownerId;
                private String currentVersion;
                private int __v;
                private String releaseVersionCode;
                private String releaseVersionId;
                private String iconMD5;
                private String icon_md5;
                private List<VersionsBean> versions;

                public TodayDownloadCountBean getTodayDownloadCount() {
                    return todayDownloadCount;
                }

                public void setTodayDownloadCount(TodayDownloadCountBean todayDownloadCount) {
                    this.todayDownloadCount = todayDownloadCount;
                }

                public GrayStrategyBean getGrayStrategy() {
                    return grayStrategy;
                }

                public void setGrayStrategy(GrayStrategyBean grayStrategy) {
                    this.grayStrategy = grayStrategy;
                }

                public boolean isIsInTrash() {
                    return isInTrash;
                }

                public void setIsInTrash(boolean isInTrash) {
                    this.isInTrash = isInTrash;
                }

                public boolean isAutoPublish() {
                    return autoPublish;
                }

                public void setAutoPublish(boolean autoPublish) {
                    this.autoPublish = autoPublish;
                }

                public String getUpdateMode() {
                    return updateMode;
                }

                public void setUpdateMode(String updateMode) {
                    this.updateMode = updateMode;
                }

                public int getTotalDownloadCount() {
                    return totalDownloadCount;
                }

                public void setTotalDownloadCount(int totalDownloadCount) {
                    this.totalDownloadCount = totalDownloadCount;
                }

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getAppName() {
                    return appName;
                }

                public void setAppName(String appName) {
                    this.appName = appName;
                }

                public String getBundleId() {
                    return bundleId;
                }

                public void setBundleId(String bundleId) {
                    this.bundleId = bundleId;
                }

                public String getPlatform() {
                    return platform;
                }

                public void setPlatform(String platform) {
                    this.platform = platform;
                }

                public String getCreator() {
                    return creator;
                }

                public void setCreator(String creator) {
                    this.creator = creator;
                }

                public String getCreatorId() {
                    return creatorId;
                }

                public void setCreatorId(String creatorId) {
                    this.creatorId = creatorId;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getShortUrl() {
                    return shortUrl;
                }

                public void setShortUrl(String shortUrl) {
                    this.shortUrl = shortUrl;
                }

                public String getCreateAt() {
                    return createAt;
                }

                public void setCreateAt(String createAt) {
                    this.createAt = createAt;
                }

                public String getOwnerId() {
                    return ownerId;
                }

                public void setOwnerId(String ownerId) {
                    this.ownerId = ownerId;
                }

                public String getCurrentVersion() {
                    return currentVersion;
                }

                public void setCurrentVersion(String currentVersion) {
                    this.currentVersion = currentVersion;
                }

                public int get__v() {
                    return __v;
                }

                public void set__v(int __v) {
                    this.__v = __v;
                }

                public String getReleaseVersionCode() {
                    return releaseVersionCode;
                }

                public void setReleaseVersionCode(String releaseVersionCode) {
                    this.releaseVersionCode = releaseVersionCode;
                }

                public String getReleaseVersionId() {
                    return releaseVersionId;
                }

                public void setReleaseVersionId(String releaseVersionId) {
                    this.releaseVersionId = releaseVersionId;
                }

                public String getIconMD5() {
                    return iconMD5;
                }

                public void setIconMD5(String iconMD5) {
                    this.iconMD5 = iconMD5;
                }

                public String getIcon_md5() {
                    return icon_md5;
                }

                public void setIcon_md5(String icon_md5) {
                    this.icon_md5 = icon_md5;
                }

                public List<VersionsBean> getVersions() {
                    return versions;
                }

                public void setVersions(List<VersionsBean> versions) {
                    this.versions = versions;
                }

                public static class TodayDownloadCountBean implements Serializable {
                    /**
                     * count : 3
                     * date : 2020-05-29T08:06:44.603Z
                     */

                    private int count;
                    private String date;

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getDate() {
                        return date;
                    }

                    public void setDate(String date) {
                        this.date = date;
                    }
                }

                public static class GrayStrategyBean implements Serializable {
                    /**
                     * ipType : black
                     * ipList : []
                     * updateMode : silent
                     */

                    private String ipType;
                    private String updateMode;
                    private List<?> ipList;

                    public String getIpType() {
                        return ipType;
                    }

                    public void setIpType(String ipType) {
                        this.ipType = ipType;
                    }

                    public String getUpdateMode() {
                        return updateMode;
                    }

                    public void setUpdateMode(String updateMode) {
                        this.updateMode = updateMode;
                    }

                    public List<?> getIpList() {
                        return ipList;
                    }

                    public void setIpList(List<?> ipList) {
                        this.ipList = ipList;
                    }
                }

                public static class VersionsBean implements Serializable {
                    private int downloadCount;
                    private boolean showOnDownloadPage;
                    private boolean isInOss;
                    private boolean isArchive;
                    private boolean isInTrash;
                    private boolean hidden;
                    private String updateMode;
                    private String _id;
                    private String versionCode;
                    private String bundleId;
                    private String versionStr;
                    private String icon;
                    private String uploader;
                    private String uploaderId;
                    private String uploadAt;
                    private String obbUploadAt;
                    private String delTime;
                    private String appId;
                    private String downloadUrl;
                    private int size;
                    private String packageMD5;
                    private String packageTag;
                    private String installUrl;
                    private String publicInstallUrl;
                    private int __v;
                    private String obbDownloadUrl;
                    private String obbMD5;
                    private int obbSize;
                    private String publicObbDownloadUrl;
                    private String changelog;
                    private String package_md5;

                    public int getDownloadCount() {
                        return downloadCount;
                    }

                    public void setDownloadCount(int downloadCount) {
                        this.downloadCount = downloadCount;
                    }

                    public boolean isShowOnDownloadPage() {
                        return showOnDownloadPage;
                    }

                    public void setShowOnDownloadPage(boolean showOnDownloadPage) {
                        this.showOnDownloadPage = showOnDownloadPage;
                    }

                    public boolean isIsInOss() {
                        return isInOss;
                    }

                    public void setIsInOss(boolean isInOss) {
                        this.isInOss = isInOss;
                    }

                    public boolean isIsArchive() {
                        return isArchive;
                    }

                    public void setIsArchive(boolean isArchive) {
                        this.isArchive = isArchive;
                    }

                    public boolean isIsInTrash() {
                        return isInTrash;
                    }

                    public void setIsInTrash(boolean isInTrash) {
                        this.isInTrash = isInTrash;
                    }

                    public boolean isHidden() {
                        return hidden;
                    }

                    public void setHidden(boolean hidden) {
                        this.hidden = hidden;
                    }

                    public String getUpdateMode() {
                        return updateMode;
                    }

                    public void setUpdateMode(String updateMode) {
                        this.updateMode = updateMode;
                    }

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public String getVersionCode() {
                        return versionCode;
                    }

                    public void setVersionCode(String versionCode) {
                        this.versionCode = versionCode;
                    }

                    public String getBundleId() {
                        return bundleId;
                    }

                    public void setBundleId(String bundleId) {
                        this.bundleId = bundleId;
                    }

                    public String getVersionStr() {
                        return versionStr;
                    }

                    public void setVersionStr(String versionStr) {
                        this.versionStr = versionStr;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }

                    public String getUploader() {
                        return uploader;
                    }

                    public void setUploader(String uploader) {
                        this.uploader = uploader;
                    }

                    public String getUploaderId() {
                        return uploaderId;
                    }

                    public void setUploaderId(String uploaderId) {
                        this.uploaderId = uploaderId;
                    }

                    public String getUploadAt() {
                        return uploadAt;
                    }

                    public void setUploadAt(String uploadAt) {
                        this.uploadAt = uploadAt;
                    }

                    public String getObbUploadAt() {
                        return obbUploadAt;
                    }

                    public void setObbUploadAt(String obbUploadAt) {
                        this.obbUploadAt = obbUploadAt;
                    }

                    public String getDelTime() {
                        return delTime;
                    }

                    public void setDelTime(String delTime) {
                        this.delTime = delTime;
                    }

                    public String getAppId() {
                        return appId;
                    }

                    public void setAppId(String appId) {
                        this.appId = appId;
                    }

                    public String getDownloadUrl() {
                        return downloadUrl;
                    }

                    public void setDownloadUrl(String downloadUrl) {
                        this.downloadUrl = downloadUrl;
                    }

                    public int getSize() {
                        return size;
                    }

                    public void setSize(int size) {
                        this.size = size;
                    }

                    public String getPackageMD5() {
                        return packageMD5;
                    }

                    public void setPackageMD5(String packageMD5) {
                        this.packageMD5 = packageMD5;
                    }

                    public String getPackageTag() {
                        return packageTag;
                    }

                    public void setPackageTag(String packageTag) {
                        this.packageTag = packageTag;
                    }

                    public String getInstallUrl() {
                        return installUrl;
                    }

                    public void setInstallUrl(String installUrl) {
                        this.installUrl = installUrl;
                    }

                    public String getPublicInstallUrl() {
                        return publicInstallUrl;
                    }

                    public void setPublicInstallUrl(String publicInstallUrl) {
                        this.publicInstallUrl = publicInstallUrl;
                    }

                    public int get__v() {
                        return __v;
                    }

                    public void set__v(int __v) {
                        this.__v = __v;
                    }

                    public String getObbDownloadUrl() {
                        return obbDownloadUrl;
                    }

                    public void setObbDownloadUrl(String obbDownloadUrl) {
                        this.obbDownloadUrl = obbDownloadUrl;
                    }

                    public String getObbMD5() {
                        return obbMD5;
                    }

                    public void setObbMD5(String obbMD5) {
                        this.obbMD5 = obbMD5;
                    }

                    public int getObbSize() {
                        return obbSize;
                    }

                    public void setObbSize(int obbSize) {
                        this.obbSize = obbSize;
                    }

                    public String getPublicObbDownloadUrl() {
                        return publicObbDownloadUrl;
                    }

                    public void setPublicObbDownloadUrl(String publicObbDownloadUrl) {
                        this.publicObbDownloadUrl = publicObbDownloadUrl;
                    }

                    public String getChangelog() {
                        return changelog;
                    }

                    public void setChangelog(String changelog) {
                        this.changelog = changelog;
                    }

                    public String getPackage_md5() {
                        return package_md5;
                    }

                    public void setPackage_md5(String package_md5) {
                        this.package_md5 = package_md5;
                    }
                }
            }
        }
    }
}
