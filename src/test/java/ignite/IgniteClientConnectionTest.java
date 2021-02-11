package ignite;


import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

public class IgniteClientConnectionTest {
    public static void main(String[] args) {
        igniteClientTest();
    }

    public static void igniteClientTest() {
//        ClientConfiguration cfg = new ClientConfiguration().setAddresses("10.101.39.183:48100");
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:48100");
        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            System.out.println();
            System.out.println(">>> Thin client put-get example started.");

            final String CACHE_NAME = "put-get-example";

            ClientCache<Integer, String> cache = igniteClient.getOrCreateCache(CACHE_NAME);

            System.out.format(">>> Created cache [%s].\n", CACHE_NAME);

            Integer key = 1;
            String val = "1545 Jackson Street";

            cache.put(key, val);

            System.out.format(">>> Saved [%s] in the cache.\n", val);

            String cachedVal = cache.get(key);

            System.out.format(">>> Loaded [%s] from the cache.\n", cachedVal);
        } catch (ClientException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.format("Unexpected failure: %s\n", e);
        }
    }
}


