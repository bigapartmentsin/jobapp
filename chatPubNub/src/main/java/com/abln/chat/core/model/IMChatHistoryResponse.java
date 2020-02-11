package com.abln.chat.core.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IMChatHistoryResponse {

    @SerializedName("entry")
    @Expose
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public class Alert {

        @SerializedName("body")
        @Expose
        private String body;
        @SerializedName("title")
        @Expose
        private String title;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    public class Aps {

        @SerializedName("content-available")
        @Expose
        private Boolean contentAvailable;
        @SerializedName("sound")
        @Expose
        private String sound;
        @SerializedName("alert")
        @Expose
        private Alert alert;
        @SerializedName("message")
        @Expose
        private Message_ message;

        public Boolean getContentAvailable() {
            return contentAvailable;
        }

        public void setContentAvailable(Boolean contentAvailable) {
            this.contentAvailable = contentAvailable;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public Alert getAlert() {
            return alert;
        }

        public void setAlert(Alert alert) {
            this.alert = alert;
        }

        public Message_ getMessage() {
            return message;
        }

        public void setMessage(Message_ message) {
            this.message = message;
        }

    }

    public class Data {

        @SerializedName("message")
        @Expose
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

    }

    public class Entry {

        @SerializedName("pn_gcm")
        @Expose
        private PnGcm pnGcm;
        @SerializedName("pn_apns")
        @Expose
        private PnApns pnApns;

        public PnGcm getPnGcm() {
            return pnGcm;
        }

        public void setPnGcm(PnGcm pnGcm) {
            this.pnGcm = pnGcm;
        }

        public PnApns getPnApns() {
            return pnApns;
        }

        public void setPnApns(PnApns pnApns) {
            this.pnApns = pnApns;
        }

    }

    public class Message {

        @SerializedName("isChatMsgSeen")
        @Expose
        private Boolean isChatMsgSeen;
        @SerializedName("isChatMsgSent")
        @Expose
        private Boolean isChatMsgSent;
        @SerializedName("msgId")
        @Expose
        private String msgId;
        @SerializedName("msgMembers")
        @Expose
        private List<MsgMember> msgMembers = null;
        @SerializedName("msgMetaData")
        @Expose
        private MsgMetaData msgMetaData;
        @SerializedName("msgMultimediaInfo")
        @Expose
        private MsgMultimediaInfo msgMultimediaInfo;
        @SerializedName("msgSenderId")
        @Expose
        private String msgSenderId;
        @SerializedName("msgText")
        @Expose
        private String msgText;
        @SerializedName("roomId")
        @Expose
        private String roomId;
        @SerializedName("msgType")
        @Expose
        private String msgType;
        @SerializedName("msgVersion")
        @Expose
        private String msgVersion;
        @SerializedName("origin")
        @Expose
        private String origin;
        @SerializedName("timeToken")
        @Expose
        private Integer timeToken;

        public Boolean getIsChatMsgSeen() {
            return isChatMsgSeen;
        }

        public void setIsChatMsgSeen(Boolean isChatMsgSeen) {
            this.isChatMsgSeen = isChatMsgSeen;
        }

        public Boolean getIsChatMsgSent() {
            return isChatMsgSent;
        }

        public void setIsChatMsgSent(Boolean isChatMsgSent) {
            this.isChatMsgSent = isChatMsgSent;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public List<MsgMember> getMsgMembers() {
            return msgMembers;
        }

        public void setMsgMembers(List<MsgMember> msgMembers) {
            this.msgMembers = msgMembers;
        }

        public MsgMetaData getMsgMetaData() {
            return msgMetaData;
        }

        public void setMsgMetaData(MsgMetaData msgMetaData) {
            this.msgMetaData = msgMetaData;
        }

        public MsgMultimediaInfo getMsgMultimediaInfo() {
            return msgMultimediaInfo;
        }

        public void setMsgMultimediaInfo(MsgMultimediaInfo msgMultimediaInfo) {
            this.msgMultimediaInfo = msgMultimediaInfo;
        }

        public String getMsgSenderId() {
            return msgSenderId;
        }

        public void setMsgSenderId(String msgSenderId) {
            this.msgSenderId = msgSenderId;
        }

        public String getMsgText() {
            return msgText;
        }

        public void setMsgText(String msgText) {
            this.msgText = msgText;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgVersion() {
            return msgVersion;
        }

        public void setMsgVersion(String msgVersion) {
            this.msgVersion = msgVersion;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public Integer getTimeToken() {
            return timeToken;
        }

        public void setTimeToken(Integer timeToken) {
            this.timeToken = timeToken;
        }

    }

    public class Message_ {

        @SerializedName("isChatMsgSeen")
        @Expose
        private Boolean isChatMsgSeen;
        @SerializedName("isChatMsgSent")
        @Expose
        private Boolean isChatMsgSent;
        @SerializedName("msgId")
        @Expose
        private String msgId;
        @SerializedName("msgMembers")
        @Expose
        private List<MsgMember_> msgMembers = null;
        @SerializedName("msgMetaData")
        @Expose
        private MsgMetaData_ msgMetaData;
        @SerializedName("msgMultimediaInfo")
        @Expose
        private MsgMultimediaInfo_ msgMultimediaInfo;
        @SerializedName("msgSenderId")
        @Expose
        private String msgSenderId;
        @SerializedName("msgText")
        @Expose
        private String msgText;
        @SerializedName("roomId")
        @Expose
        private String roomId;
        @SerializedName("msgType")
        @Expose
        private String msgType;
        @SerializedName("msgVersion")
        @Expose
        private String msgVersion;
        @SerializedName("origin")
        @Expose
        private String origin;
        @SerializedName("timeToken")
        @Expose
        private Integer timeToken;

        public Boolean getIsChatMsgSeen() {
            return isChatMsgSeen;
        }

        public void setIsChatMsgSeen(Boolean isChatMsgSeen) {
            this.isChatMsgSeen = isChatMsgSeen;
        }

        public Boolean getIsChatMsgSent() {
            return isChatMsgSent;
        }

        public void setIsChatMsgSent(Boolean isChatMsgSent) {
            this.isChatMsgSent = isChatMsgSent;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public List<MsgMember_> getMsgMembers() {
            return msgMembers;
        }

        public void setMsgMembers(List<MsgMember_> msgMembers) {
            this.msgMembers = msgMembers;
        }

        public MsgMetaData_ getMsgMetaData() {
            return msgMetaData;
        }

        public void setMsgMetaData(MsgMetaData_ msgMetaData) {
            this.msgMetaData = msgMetaData;
        }

        public MsgMultimediaInfo_ getMsgMultimediaInfo() {
            return msgMultimediaInfo;
        }

        public void setMsgMultimediaInfo(MsgMultimediaInfo_ msgMultimediaInfo) {
            this.msgMultimediaInfo = msgMultimediaInfo;
        }

        public String getMsgSenderId() {
            return msgSenderId;
        }

        public void setMsgSenderId(String msgSenderId) {
            this.msgSenderId = msgSenderId;
        }

        public String getMsgText() {
            return msgText;
        }

        public void setMsgText(String msgText) {
            this.msgText = msgText;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgVersion() {
            return msgVersion;
        }

        public void setMsgVersion(String msgVersion) {
            this.msgVersion = msgVersion;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public Integer getTimeToken() {
            return timeToken;
        }

        public void setTimeToken(Integer timeToken) {
            this.timeToken = timeToken;
        }

    }

    public class MsgMember {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("msgDelivered")
        @Expose
        private Integer msgDelivered;
        @SerializedName("msgId")
        @Expose
        private String msgId;
        @SerializedName("msgRead")
        @Expose
        private Integer msgRead;
        @SerializedName("msgUserId")
        @Expose
        private String msgUserId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getMsgDelivered() {
            return msgDelivered;
        }

        public void setMsgDelivered(Integer msgDelivered) {
            this.msgDelivered = msgDelivered;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public Integer getMsgRead() {
            return msgRead;
        }

        public void setMsgRead(Integer msgRead) {
            this.msgRead = msgRead;
        }

        public String getMsgUserId() {
            return msgUserId;
        }

        public void setMsgUserId(String msgUserId) {
            this.msgUserId = msgUserId;
        }

    }

    public class MsgMember_ {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("msgDelivered")
        @Expose
        private Integer msgDelivered;
        @SerializedName("msgId")
        @Expose
        private String msgId;
        @SerializedName("msgRead")
        @Expose
        private Integer msgRead;
        @SerializedName("msgUserId")
        @Expose
        private String msgUserId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getMsgDelivered() {
            return msgDelivered;
        }

        public void setMsgDelivered(Integer msgDelivered) {
            this.msgDelivered = msgDelivered;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public Integer getMsgRead() {
            return msgRead;
        }

        public void setMsgRead(Integer msgRead) {
            this.msgRead = msgRead;
        }

        public String getMsgUserId() {
            return msgUserId;
        }

        public void setMsgUserId(String msgUserId) {
            this.msgUserId = msgUserId;
        }

    }

    public class MsgMetaData {


    }

    public class MsgMetaData_ {


    }

    public class MsgMultimediaInfo {


    }

    public class MsgMultimediaInfo_ {


    }

    public class PnApns {

        @SerializedName("aps")
        @Expose
        private Aps aps;

        public Aps getAps() {
            return aps;
        }

        public void setAps(Aps aps) {
            this.aps = aps;
        }

    }

    public class PnGcm {

        @SerializedName("data")
        @Expose
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

    }
}
