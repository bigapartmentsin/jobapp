package com.abln.futur.common;


public class CustomDateToString {
    public static String month(String _date) {
        try {
            if (_date != null && !_date.equals("null") && !_date.isEmpty()) {

                String[] splitDate = _date.split("\\/");
                String monthStr = "";

                switch (Integer.parseInt(splitDate[0])) {
                    case 1:
                        monthStr = "Jan";
                        break;
                    case 2:
                        monthStr = "Feb";
                        break;
                    case 3:
                        monthStr = "Mar";
                        break;
                    case 4:
                        monthStr = "Apr";
                        break;
                    case 5:
                        monthStr = "May";
                        break;
                    case 6:
                        monthStr = "Jun";
                        break;
                    case 7:
                        monthStr = "Jul";
                        break;
                    case 8:
                        monthStr = "Aug";
                        break;
                    case 9:
                        monthStr = "Sep";
                        break;
                    case 10:
                        monthStr = "Oct";
                        break;
                    case 11:
                        monthStr = "Nov";
                        break;
                    case 12:
                        monthStr = "Dec";
                        break;
                }

                return splitDate[1] + "-" + monthStr + "-" + splitDate[2];

            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    public static String date(String _date) {
        try {
            if (_date != null && !_date.equals("null") && !_date.isEmpty()) {

                String[] splitDate = _date.split("\\/");
                return "" + Integer.parseInt(splitDate[1]);

            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }
}
