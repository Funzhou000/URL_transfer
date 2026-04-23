public class main {
    static void main() {
        FakeURLMaker fakeURLMaker = new FakeURLMaker();
        Encoding encoding= new Encoding();
        long startTime = System.currentTimeMillis();
        Search search = new Search("XljTderu");
        System.out.println("Done! Total time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}