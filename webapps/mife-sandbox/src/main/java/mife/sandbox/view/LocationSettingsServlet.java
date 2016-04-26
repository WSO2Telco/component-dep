/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.LocationCtrlFunctions;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.LocationImpl;
import mife.sandbox.model.entities.Locationparam;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.LocationFunctions;

/**
 *
 * @author User
 */
public class LocationSettingsServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (requestParts[0].equals("GetLocData")) {
            String userid = request.getParameter("userid").replaceAll("\\s", "");
            int user_id = new LocationImpl().getUser(userid).getId(); //((User) LocationImpl.getUser(userid)).getId();
            
            Locationparam locParam = LocationCtrlFunctions.getUserLocationSettings(user_id);
            if (locParam != null){
                out.println(locParam.getAltitude()+","+locParam.getLatitude()+","+locParam.getLongitude()+","+locParam.getLocationRetrieveStatus());
            } else {
                out.println("NODATA");
            }
            
        } else if (requestParts[0].equals("PerformLocationSettings")) {

            String alt = request.getParameter("altitude");
            String lat = request.getParameter("latitude");
            String longi = request.getParameter("longitude");
            String locStatus = request.getParameter("locStatus");

            String userid = request.getParameter("userid").replaceAll("\\s", "");
            int user_id = new LocationImpl().getUser(userid).getId(); //int user_id = ((User) LocationImpl.getUser(userid)).getId();

            String return_val = "";

            if (LocationCtrlFunctions.isLocationSettingsExists(user_id)) {
                //update Settings
                Locationparam locParam = LocationCtrlFunctions.getUserLocationSettings(user_id);
                locParam.setAltitude(alt);
                locParam.setLatitude(lat);
                locParam.setLongitude(longi);
                locParam.setLocationRetrieveStatus(locStatus);                

                boolean receivedData = LocationFunctions.editLocation(user_id, locParam);
                if (receivedData) {
                    return_val = "true";
                } else {
                    return_val = "false";
                }
                System.out.println(return_val);
            } else {
                Locationparam loc = new Locationparam();
                loc.setAltitude(alt);
                loc.setLongitude(longi);
                loc.setLatitude(lat);
                loc.setLocationRetrieveStatus(locStatus);
                loc.setUser(UserControlFunctions.getUserObj(user_id));

                int receivedData = LocationFunctions.saveLocation(loc);
                if (receivedData == 201) {
                    return_val = "true";
                } else {
                    return_val = "false";
                }
                System.out.println(return_val);
            }

            try {
                out.println(return_val);
            } finally {
                out.close();
            }

//        } else if (requestParts[0].equals("EditLocData")) {
//
//            String alt = request.getParameter("altitude");
//            String lat = request.getParameter("latitude");
//            String longi = request.getParameter("longitude");
//            String locStatus = request.getParameter("locStatus");
//
//            String userid = request.getParameter("userid").replaceAll("\\s", "");
//            int user_id = ((User) LocationImpl.getUser(userid)).getId();

        } else if (requestParts[0].equals("SaveLocData")) {
            String alt = request.getParameter("altitude");
            String lat = request.getParameter("latitude");
            String longi = request.getParameter("longitude");
            String locStatus = request.getParameter("locStatus");

            String return_val = "";

            Locationparam loc = new Locationparam();
            loc.setAltitude(alt);
            loc.setLongitude(longi);
            loc.setLatitude(lat);
            loc.setLocationRetrieveStatus(locStatus);

            int receivedData = LocationFunctions.saveLocation(loc);
            if (receivedData == 201) {
                return_val = "true";
            } else {
                return_val = "false";
            }
            System.out.println(return_val);

            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        }
    }

    private static String[] getRequestParts(HttpServletRequest request) {
        String[] requestParts = null;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }
        return requestParts;
    }
}
