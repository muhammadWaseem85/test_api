package web.config;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "configs/PrepilotConfig.properties";


    public ConfigFileReader(){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

// --------------------------------------------------------------------------------------------------------
// -----------------------------    Web   ---------------------------------------------
// --------------------------------------------------------------------------------------------------------

    public long getImplicitlyWait() {
        String implicitlyWait = properties.getProperty("implicitlyWait");
        if(implicitlyWait != null) return Long.parseLong(implicitlyWait);
        else throw new RuntimeException("implicitlyWait not specified in the Configuration.properties file.");
    }

    // Application url
    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if(url != null) return url;
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }

    // Dashboard url
    public String getHomeUrl() {
        String homeUrl = properties.getProperty("homeUrl");
        if(homeUrl != null) return homeUrl;
        else throw new RuntimeException("home url not specified in the Configuration.properties file.");
    }


    // Admin username
    public String getAdminUsername() {
        String adminUsername = properties.getProperty("adminUsername");
        if(adminUsername != null) return adminUsername;
        else throw new RuntimeException("adminUsername not specified in the Configuration.properties file.");
    }

    //Admin password
    public String getAdminPassword() {
        String adminPassword = properties.getProperty("adminPassword");
        if(adminPassword != null) return adminPassword;
        else throw new RuntimeException("adminPassword not specified in the Configuration.properties file.");
    }


// --------------------------------------------------------------------------------------------------------
// -----------------------------    API   ---------------------------------------------
// --------------------------------------------------------------------------------------------------------


    // Api end user url
    public String getEndUserApiUrl() {
        String endUserApiUrl  = properties.getProperty("endUserApiUrl");
        if(endUserApiUrl != null) return endUserApiUrl;
        else throw new RuntimeException("End user api not specified in the Configuration.properties file.");
    }

    // Api end user url
    public String getEndUserApiUrl2() {
        String endUserApiUrl  = properties.getProperty("endUserApiUrl2");
        if(endUserApiUrl != null) return endUserApiUrl;
        else throw new RuntimeException("End user api not specified in the Configuration.properties file.");
    }

    // Api shipment url
    public String getShipmentApiUrl() {
        String shipmentApiUrl  = properties.getProperty("shipmentApiUrl");
        if(shipmentApiUrl != null) return shipmentApiUrl ;
        else throw new RuntimeException("Api shipment url not specified in the Configuration.properties file.");
    }

    // Api courier url
    public String getCourierApiUrl() {
        String courierApiUrl  = properties.getProperty("courierApiUrl");
        if(courierApiUrl != null) return courierApiUrl ;
        else throw new RuntimeException("Api courierApiUrl not specified in the Configuration.properties file.");
    }

    public String getSpId() {
        String spId  = properties.getProperty("spId");
        if(spId != null) return spId ;
        else throw new RuntimeException("spId  not specified in the Configuration.properties file.");
    }

    public String getAltSpID() {
        String altSpId  = properties.getProperty("altSpId");
        if(altSpId != null) return altSpId ;
        else throw new RuntimeException("altSpId  not specified in the Configuration.properties file.");
    }

    public String getAnotherSpID() {
        String altSpId  = properties.getProperty("anotherSpID");
        if(altSpId != null) return altSpId ;
        else throw new RuntimeException("altSpId  not specified in the Configuration.properties file.");
    }


    public String getPhNumber() {
        String phNumber  = properties.getProperty("phNumber");
        if(phNumber != null) return phNumber ;
        else throw new RuntimeException("phNumber  not specified in the Configuration.properties file.");
    }

    public String getPIN() {
        String pin  = properties.getProperty("PIN");
        if(pin != null) return pin ;
        else throw new RuntimeException("pin  not specified in the Configuration.properties file.");
    }

    public String getApiKey() {
        String apiKey  = properties.getProperty("apiKey");
        if(apiKey != null) return apiKey ;
        else throw new RuntimeException("X-Api-Key  not specified in the Configuration.properties file.");
    }

    public String getApiKeyForEndUser() {
        String apiKey  = properties.getProperty("apiKeyForEndUser");
        if(apiKey != null) return apiKey ;
        else throw new RuntimeException("apiKeyForEndUser not specified in the Configuration.properties file.");
    }

    public String getApiKeyForReject() {
        String apiKey  = properties.getProperty("apiKeyForReject");
        if(apiKey != null) return apiKey ;
        else throw new RuntimeException("X-Api-Key  not specified in the Configuration.properties file.");
    }

    public String getApiKeyForQue() {
        String apiKey  = properties.getProperty("apiKeyForQue");
        if(apiKey != null) return apiKey ;
        else throw new RuntimeException("X-Api-Key  not specified in the Configuration.properties file.");
    }

    public String getApiKeyForReAllocate() {
        String apiKey  = properties.getProperty("apiKeyForReallocate");
        if(apiKey != null) return apiKey ;
        else throw new RuntimeException("X-Api-Key  not specified in the Configuration.properties file.");
    }

    public String getPieceGroupCode() {
        String pieceGroupCode  = properties.getProperty("pieceGroupCode");
        if(pieceGroupCode!= null) return pieceGroupCode ;
        else throw new RuntimeException("PieceGroupCode  not specified in the Configuration.properties file.");
    }

// -----------------------------------------------------------------------------------
// ------------------ Pieces for Hard Allocation --------------------
// -----------------------------------------------------------------------------------

    public String getFirstPiece() {
        String firstPiece  = properties.getProperty("firstPiece");
        if(firstPiece != null) return firstPiece ;
        else throw new RuntimeException("firstPiece  not specified in the Configuration.properties file.");
    }

    public String getSecondPiece() {
        String secondPiece  = properties.getProperty("secondPiece");
        if(secondPiece != null) return secondPiece ;
        else throw new RuntimeException("secondPiece  not specified in the Configuration.properties file.");
    }

    public String getThirdPiece() {
        String thirdPiece  = properties.getProperty("thirdPiece");
        if(thirdPiece != null) return thirdPiece;
        else throw new RuntimeException("thirdPiece  not specified in the Configuration.properties file.");
    }

    public String getFourthPiece() {
        String fourthPiece  = properties.getProperty("fourthPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getFifthPiece() {
        String fourthPiece  = properties.getProperty("fifthPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getSixthPiece() {
        String fourthPiece  = properties.getProperty("sixthPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getSeventhPiece() {
        String fourthPiece  = properties.getProperty("seventhPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getEightPiece() {
        String fourthPiece  = properties.getProperty("eightPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getNinthPiece() {
        String fourthPiece  = properties.getProperty("ninthPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }

    public String getTenthPiece() {
        String fourthPiece  = properties.getProperty("tenthPiece");
        if(fourthPiece != null) return fourthPiece;
        else throw new RuntimeException("fourthPiece  not specified in the Configuration.properties file.");
    }


// -----------------------------------------------------------------------------------
// ------------------ Pieces for Soft Allocation --------------------
// -----------------------------------------------------------------------------------


    public String get0Piece() {
        String firstPiece  = properties.getProperty("0Piece");
        if(firstPiece != null) return firstPiece ;
        else throw new RuntimeException("0Piece  not specified in the Configuration.properties file.");
    }

    public String get1stPiece() {
        String firstPiece  = properties.getProperty("1stPiece");
        if(firstPiece != null) return firstPiece ;
        else throw new RuntimeException("firstPiece  not specified in the Configuration.properties file.");
    }

    public String get2ndPiece() {
        String secondPiece  = properties.getProperty("2ndPiece");
        if(secondPiece != null) return secondPiece ;
        else throw new RuntimeException("secondPiece  not specified in the Configuration.properties file.");
    }

    public String get3rdPiece() {
        String thirdPiece  = properties.getProperty("3rdPiece");
        if(thirdPiece != null) return thirdPiece;
        else throw new RuntimeException("thirdPiece  not specified in the Configuration.properties file.");
    }

    public String get4thPiece() {
        String thirdPiece  = properties.getProperty("4thPiece");
        if(thirdPiece != null) return thirdPiece;
        else throw new RuntimeException("thirdPiece  not specified in the Configuration.properties file.");
    }

    public String get5thPiece() {
        String thirdPiece  = properties.getProperty("5thPiece");
        if(thirdPiece != null) return thirdPiece;
        else throw new RuntimeException("5thPiece  not specified in the Configuration.properties file.");
    }

    public String get6thPiece() {
        String thirdPiece  = properties.getProperty("6thPiece");
        if(thirdPiece != null) return thirdPiece;
        else throw new RuntimeException("6thPiece  not specified in the Configuration.properties file.");
    }

    public String get7thPiece() {
        String Piece  = properties.getProperty("7thPiece");
        if(Piece != null) return Piece;
        else throw new RuntimeException("Piece  not specified in the Configuration.properties file.");
    }


// --------------------------------------------------------------------------------------------------------
// -----------------------------    Service point capacity  -----------------------------------------------------------
// --------------------------------------------------------------------------------------------------------


    public String getSmallSize() {
        String size  = properties.getProperty("smallSize");
        if(size != null) return size ;
        else throw new RuntimeException("SmallSize  not specified in the Configuration.properties file.");
    }

    public String getMediumSize() {
        String size  = properties.getProperty("mediumSize");
        if(size != null) return size ;
        else throw new RuntimeException("MediumSize  not specified in the Configuration.properties file.");
    }

    public String getLargeSize() {
        String size  = properties.getProperty("largeSize");
        if(size != null) return size ;
        else throw new RuntimeException("LargeSize  not specified in the Configuration.properties file.");
    }


    public String getLockerDepth() {
        String size  = properties.getProperty("lockerDepth");
        if(size != null) return size ;
        else throw new RuntimeException("lockerDepth  not specified in the Configuration.properties file.");
    }

    public String getLockerWidth() {
        String size  = properties.getProperty("lockerWidth");
        if(size != null) return size ;
        else throw new RuntimeException("lockerWidth  not specified in the Configuration.properties file.");
    }


    public String getHeightOfSmallLocker() {
        String size  = properties.getProperty("heightOfSmallLocker");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfSmallLocker  not specified in the Configuration.properties file.");
    }

    public String getHeightOfMediumLocker() {
        String size  = properties.getProperty("heightOfMediumLocker");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfMediumLocker  not specified in the Configuration.properties file.");
    }

    public String getHeightOfLargeLocker() {
        String size  = properties.getProperty("heightOfLargeLocker");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfLargeLocker  not specified in the Configuration.properties file.");
    }

    public String getCompartmentsInSmall() {
        String size  = properties.getProperty("compartmentsInSmall");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfLargeLocker  not specified in the Configuration.properties file.");
    }

    public String getCompartmentsInMedium() {
        String size  = properties.getProperty("compartmentsInMedium");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfLargeLocker  not specified in the Configuration.properties file.");
    }

    public String getCompartmentsInLarge() {
        String size  = properties.getProperty("compartmentsInLarge");
        if(size != null) return size ;
        else throw new RuntimeException("heightOfLargeLocker  not specified in the Configuration.properties file.");
    }



// --------------------------------------------------------------------------------------------------------
// -----------------------------    Database   -----------------------------------------------------------
// --------------------------------------------------------------------------------------------------------


    public String getDbUrl() {
        String dbUrl  = properties.getProperty("dbUrl");
        if(dbUrl != null) return dbUrl ;
        else throw new RuntimeException("dbUrl  not specified in the Configuration.properties file.");
    }

    public String getDbName() {
        String dbName  = properties.getProperty("dbName");
        if(dbName != null) return dbName ;
        else throw new RuntimeException("dbName  not specified in the Configuration.properties file.");
    }

    public String getDbUser() {
        String dbUsername  = properties.getProperty("dbUsername");
        if(dbUsername != null) return dbUsername ;
        else throw new RuntimeException("dbUsername  not specified in the Configuration.properties file.");
    }

    public String getDbPassword() {
        String dbPassword  = properties.getProperty("dbPassword");
        if(dbPassword != null) return dbPassword ;
        else throw new RuntimeException("dbPassword  not specified in the Configuration.properties file.");
    }

    public String getSshHostName() {
        String dbPassword  = properties.getProperty("sshHostName");
        if(dbPassword != null) return dbPassword ;
        else throw new RuntimeException("sshHostName not specified in the Configuration.properties file.");
    }

    public String getSshUser() {
        String dbPassword  = properties.getProperty("sshUser");
        if(dbPassword != null) return dbPassword ;
        else throw new RuntimeException("sshUser not specified in the Configuration.properties file.");
    }

}