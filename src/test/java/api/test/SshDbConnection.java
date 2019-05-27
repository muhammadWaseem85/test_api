package api.test;


import java.sql.*;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.testng.Assert;
import web.config.ConfigFileReader;


public class SshDbConnection {
    ConfigFileReader conf = new ConfigFileReader();
    private static Statement stmt;
    /**
     * Java Program to connect to remote database through SSH using port forwarding
     * @author Pankaj@JournalDev
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {


        int lport=5656;
        String rhost="prepilotprod.ceolvlnrskjf.eu-central-1.rds.amazonaws.com";
        String host="ec2-3-122-177-164.eu-central-1.compute.amazonaws.com";
        int rport=3306;
        String user="ec2-user";
        String password="";
        String dbuserName = "prepilotuser";
        String dbpassword = "?8EuA03Aug)YR18(LW";
        String url = "jdbc:mysql://localhost:"+lport+"/infinity_prepilot";
        String driverName="com.mysql.cj.jdbc.Driver";
        String SshKeyFilepath = "D:\\infinity_db_key_pair.pem";
        Connection conn = null;
        Session session= null;
        try{
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session=jsch.getSession(user, host, 22);
            jsch.addIdentity(SshKeyFilepath);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println(session);
            System.out.println("Connected");
            int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
            System.out.println("Port Forwarded");

            //mysql database connectivity
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection (url, dbuserName, dbpassword);
            stmt = conn.createStatement();
            System.out.println();
            System.out.println ("Database connection established");
            System.out.println("DONE");
            ConfigFileReader conf = new ConfigFileReader();
            try{
                String query = "SELECT * FROM piece" ;
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    System.out.print("\t" + res.getString(1));
//                    String barcode = res.getString("piece_barcode");
//                    String alloc_type = res.getString("alloc_type");
//                    String piece_type = res.getString("piece_type");
//                    String sp_id = res.getString("service_point_id");
//                    String compartment_size = res.getString("compartment_size");
//                    String width = res.getString("width");
//                    String height = res.getString("height");
//                    String depth = res.getString("depth");
//                    Assert.assertEquals(conf.get1stPiece(), barcode);
//                    Assert.assertEquals(alloc_type, "2");
//                    Assert.assertEquals(piece_type, "1");
//                    Assert.assertEquals(sp_id, conf.getSpId());
//                    Assert.assertEquals(compartment_size, null);
//                    Assert.assertEquals(width, null);
//                    Assert.assertEquals(height, null);
//                    Assert.assertEquals(depth, null);
                }
            }      catch(Exception e)
            {
                e.printStackTrace();
            }



        }catch(Exception e){
            e.printStackTrace();
        }

        finally{
            if(conn != null && !conn.isClosed()){
                System.out.println("Closing Database Connection");
                conn.close();
            }
            if(session !=null && session.isConnected()){
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
    }

}
