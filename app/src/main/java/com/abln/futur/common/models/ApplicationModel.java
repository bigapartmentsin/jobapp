package com.abln.futur.common.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationModel {


    private Integer statuscode ;
    private String statusmessage;
    private Data data;
    private Map<String ,Object> addtionalProperties = new HashMap<String , Object>();


    public Integer getStatuscode(){
        return  statuscode;
    }


    public void setStatuscode(Integer statuscode){
        this.statuscode = statuscode;
    }

    public String getStatusMessage (){
        return statusmessage;

    }


    public Data getData(){
        return data;
    }

    public void setData(){
        this.data = data ;
    }



    public Map<String , Object> getAdditionProperties(){

      return  this.addtionalProperties;
    }


    public void setAddtionalProperties(String name, Object value){
        this.addtionalProperties.put(name,value);
    }




    public class CanDetails{
        private String fname;
        private String avatar;
        private String file;
        private Map<String ,Object> additionalProperties = new HashMap<String , Object>();


        public String getFname(){
            return fname;
        }


        public void setFname(String fname){
            this.fname = fname;
        }


        public String getAvatar(){
            return avatar;
        }

        public void setAvatar(String avatar){
            this.avatar = avatar;
        }

        public String getFile(){
            return file;

        }
        public  void setFile(String file){
            this.file = file;
        }


        public Map<String , Object> getAdditionalProperties(){
         return this.additionalProperties;
        }


        public void setAdditionalProperties(String name ,Object value){
            this.additionalProperties.put(name,value);
        }

    }


    public class Data{
        private String tstat;
        private ArrayList<ApplicationList> applist ;
        private Map<String ,Object> additionalProperites = new HashMap<String,Object>();

        public String getTstat(){
            return tstat;

        }
        public void setTstat(String tstat){
            this.tstat = tstat;
        }


        public ArrayList<ApplicationList> getUserList(){
            return applist;
        }

        public void setUserList(ArrayList<ApplicationList> userappllist){
            this.applist = userappllist;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperites;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperites.put(name, value);


    }


    public class ApplicationList{
            private String idd;
            private String recID;
            private String postID;
            private String canID;
            private CanDetails canDetails;
            private String astat;
            private String sstat;
            private String acc;
            private String rej ;
            private String cstat;
            private String rstat;
            private String date;
            private String time ;
            private String createdAt;
            private Map<String , Object> additinalProperteis = new HashMap<String ,Object>();


            public String getIdd(){
                return idd;
            }

            public void setIdd(String idd){
                this.idd = idd;
            }

            public String getRecID(){
                return  recID;
            }

            public void setRecID(String recID){
                this.recID = recID;
            }


            public String getCanID(){
                return  canID;
            }
            public void setCanID(String canID){
                this.canID = canID;
            }

            public CanDetails getCanDetails(){
                return  canDetails;
            }


            public void setCanDetails(CanDetails canDetails){
                this.canDetails  =canDetails;
            }

            public String getAstat(){
                return astat;
            }

            public void setAstat(String astat){
                this.astat = astat ;
            }

            public String getSstat(){
                return sstat;
            }


            public void setSstat(String sstat){
                this.sstat  = sstat;
            }

            public String getAcc(){
                return acc;
            }

            public void setAcc(String acc){
                this.acc = acc;
            }


            public String getRej(){
                return  rej ;
            }

            public void setRej(String rej ){
                this.rej  = rej ;
            }



            public String getCstat(){
                return cstat;
            }

            public void setCstat(String cstat){
                this.cstat  =cstat ;
            }


            public String getRstat(){

                return rstat ;
            }


            public String getDate(){
                return  date ;
            }

            public void setDate(String date){
                this.date  = date ;
            }


            public String getTime(){
                return  time;
            }


            public void setTime(String time){
                this.time = time ;
            }

            public String getCreatedAt(){
                return  createdAt ;
            }


            public void setCreatedAt(String createdAt){
                this.createdAt = createdAt ;
            }

            public Map<String , Object> getAdditinalProperteis(){
                return  this.additinalProperteis;
            }

            public void setAdditinalProperteis(String name , Object value ){
                this.additinalProperteis.put(name,value);
            }

    }

    }


}



