package lab3_2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewsReaderFactory.class, ConfigurationLoader.class})
public class NewsLoaderTest {
    
    ConfigurationLoader configurationLoader;
    Configuration configuration;
    NewsReaderFactory newsReaderFactory;
    NewsReader newsReader;
    NewsLoader newsLoader;
    Field field;
    
    @SuppressWarnings("static-access")
    @Before
    public void setUp() {
    	PowerMockito.mockStatic(ConfigurationLoader.class);
    	PowerMockito.mockStatic(NewsReaderFactory.class);
        configurationLoader = mock(ConfigurationLoader.class);
        configuration = mock(Configuration.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        newsReader = mock(NewsReader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);
        when(newsReaderFactory.getReader(anyString())).thenReturn(newsReader);
        newsLoader = new NewsLoader();
        try {
            field = PublishableNews.class.getDeclaredField("publicContent");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void incomingNewsWithNoneSubscriptionShouldBeKeptInPublicContent() {
        IncomingInfo incomingInfo = new IncomingInfo("test", SubsciptionType.NONE);
        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(incomingInfo);
        when(newsReader.read()).thenReturn(incomingNews);
        try {
            int sizeOfPublicContent = ((List<String>) field.get(newsLoader.loadNews())).size();
            assertThat(sizeOfPublicContent, Matchers.is(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}