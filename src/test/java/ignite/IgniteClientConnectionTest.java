package ignite;


import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.junit.Test;

public class IgniteClientConnectionTest {

    @Test
    public void igniteClientTest() {
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            for (int i = 0; i < 1; i++) {
                System.out.println();
                System.out.println(">>> Thin client put-get example started.");

                final String CACHE_NAME = "put-get-example" + i;

                ClientCache<Integer, String> cache = igniteClient.getOrCreateCache(CACHE_NAME);

                System.out.format(">>> Created cache [%s].\n", CACHE_NAME);

                Integer key = i;
                String val = "1545 Jackson Street";

                cache.put(key, val);

                System.out.format(">>> Saved [%s] in the cache.\n", val);

                String cachedVal = cache.get(key);

                System.out.format(">>> Loaded [%s] from the cache.\n", cachedVal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


