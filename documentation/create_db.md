
public class Main {
public static void main(String[] args){

Full script for creating new DB
Timestamp ts = new Timestamp(System.currentTimeMillis());
ApiCall apiCall = new ApiCall();
SqlDriver sqlDriver = new SqlDriver();

        apiCall.getToken();
        apiCall.createFullAcutionList();
        sqlDriver.createDBFromItems(apiCall.auctions_items);

        apiCall.createNewPriceFromAuction();
        sqlDriver.addAllItemsPriceFromAuctionToDB(apiCall.auctions, ts);
        

        SqlDriver sqlDriver = new SqlDriver();
        sqlDriver.getItemFromDB(4358);
        sqlDriver.getLastItemsPriceFromDB(4358);


    }
}
