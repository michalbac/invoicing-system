package pl.futurecollars.invoicing.db

class FileBasedDbTestSuite extends DatabaseTestSuite{
    def setup(){
        db = new FileBasedDatabase();
    }
}
