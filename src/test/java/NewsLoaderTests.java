import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTests {
    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoader;
    private Configuration configuration;
    private NewsReader newsReader;
    private IncomingNews incomingNews;
    private Field contentField;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        configuration = mock(Configuration.class);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);
        when(configuration.getReaderType()).thenReturn("mock");

        mockStatic(NewsReaderFactory.class);
        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader(Mockito.<String>any())).thenReturn(newsReader);


    }

    @Test
    public void shouldReturnCorrectNumberOfPublicNews() {
        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("Content 1", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Content 2", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Content 3", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Content 4", SubsciptionType.A));
        when(newsReader.read()).thenReturn(incomingNews);

        PublishableNews publishableNews = newsLoader.loadNews();
        try {
            contentField = PublishableNews.class.getDeclaredField("publicContent");
            contentField.setAccessible(true);
            assertThat(((List<String>) contentField.get(publishableNews)).size(), is(3));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldReturnCorrectNumberOfSubscribedNews() {
        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("Content 1", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("Content 2", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Content 3", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Content 4", SubsciptionType.A));
        when(newsReader.read()).thenReturn(incomingNews);

        PublishableNews publishableNews = newsLoader.loadNews();
        try {
            contentField = PublishableNews.class.getDeclaredField("subscribentContent");
            contentField.setAccessible(true);
            assertThat(((List<String>) contentField.get(publishableNews)).size(), is(2));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void loadNewsMethodShouldLoadConfigurationOnce() {
        incomingNews = new IncomingNews();
        when(newsReader.read()).thenReturn(incomingNews);

        newsLoader.loadNews();
        Mockito.verify(configurationLoader, times(1)).loadConfiguration();
    }
}
