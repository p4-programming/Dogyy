package com.bnb.doggydoo.mydog.datasource.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getDistressPinByUserID {
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }


    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("pet_image")
        @Expose
        private String petImage;
        @SerializedName("pet_description")
        @Expose
        private String petDescription;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("lattitute")
        @Expose
        private String lattitute;
        @SerializedName("longitute")
        @Expose
        private String longitute;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("update_date")
        @Expose
        private String updateDate;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("resolvedOption")
        @Expose
        private String resolvedOption;
        @SerializedName("resolvedMessage")
        @Expose
        private String resolvedMessage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPetImage() {
            return petImage;
        }

        public void setPetImage(String petImage) {
            this.petImage = petImage;
        }

        public String getPetDescription() {
            return petDescription;
        }

        public void setPetDescription(String petDescription) {
            this.petDescription = petDescription;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLattitute() {
            return lattitute;
        }

        public void setLattitute(String lattitute) {
            this.lattitute = lattitute;
        }

        public String getLongitute() {
            return longitute;
        }

        public void setLongitute(String longitute) {
            this.longitute = longitute;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getResolvedOption() {
            return resolvedOption;
        }

        public void setResolvedOption(String resolvedOption) {
            this.resolvedOption = resolvedOption;
        }

        public String getResolvedMessage() {
            return resolvedMessage;
        }

        public void setResolvedMessage(String resolvedMessage) {
            this.resolvedMessage = resolvedMessage;
        }

    }
}
