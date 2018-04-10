import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewsReaderFactory.class, ConfigurationLoader.class, PublishableNews.class})
public class NewsLoaderTest {

    private ConfigurationLoader configLoader;
    private NewsLoader newsLoader;
    private Configuration config;
    private NewsReader reader;
    private IncomingNews incomingNews;
    private IncomingNews onlyPublishableNews;
    private PublishableNews publishable;
    private Field field;

    @Before
    public void setup(){
        configLoader = PowerMockito.mock(ConfigurationLoader.class);
        config = new Configuration();
        newsLoader = new NewsLoader();
        reader = PowerMockito.mock(FileNewsReader.class);
        incomingNews = new IncomingNews();
        onlyPublishableNews = new IncomingNews();

        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);

        PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(configLoader);
        PowerMockito.when(configLoader.loadConfiguration()).thenReturn(config);
        PowerMockito.when(NewsReaderFactory.getReader(config.getReaderType())).thenReturn(reader);
        PowerMockito.when(reader.read()).thenReturn(incomingNews);

    }
    @Test
    public void resultIncommingNewsShouldContainCorrectNumberOfNews(){
        incomingNews.add(new IncomingInfo("incInfoA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("incInfoB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("incInfoC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("incInfoNONE", SubsciptionType.NONE));

        IncomingNews resultIncomingNews = reader.read();
        assertThat(resultIncomingNews.elems().size(), is(4));
    }

    @Test
    public void loadNewsShouldReturnOnlyPublicNews(){
        incomingNews.add(new IncomingInfo("incInfoA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("incInfoB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("incInfoC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("incInfoNONE", SubsciptionType.NONE));

        try {
            field = PublishableNews.class.getDeclaredField("publicContent");
            field.setAccessible(true);

            List<String> result = (List<String>) field.get(newsLoader.loadNews());
            assertThat(result.size(), is(1));
        }catch(Exception e){
            System.out.println("Casting to List exception");
        }
    }

    @Test
    public void loadNewsShouldReturnEmptyListWhenIncommingNewsEmpty(){
        try {
            field = PublishableNews.class.getDeclaredField("publicContent");
            field.setAccessible(true);

            List<String> result = (List<String>) field.get(newsLoader.loadNews());
            assertThat(result.size(), is(0));
        }catch(Exception e){
            System.out.println("Casting to List exception");
        }
    }
}
