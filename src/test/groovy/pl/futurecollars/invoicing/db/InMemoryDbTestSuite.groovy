package pl.futurecollars.invoicing.db

class InMemoryDbTestSuite extends DatabaseTestSuite{

    def setup(){
        db = new InMemoryDatabase();
    }

}