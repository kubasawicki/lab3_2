package edu.iis.mto.staticmock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewsReaderFactory.class, ConfigurationLoader.class})
public class NewsLoaderTests {

    private NewsReaderFactory newsReaderFactory;
    private ConfigurationLoader configurationLoader;
    private Configuration config;
    private NewsReader reader;
    private NewsLoader newsLoader;
    private Field field;

    @SuppressWarnings("static-access")
    @Before
    public void setUp() {
        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        reader = mock(NewsReader.class);
        config = mock(Configuration.class);
        when(config.getReaderType()).thenReturn("");
        when(configurationLoader.loadConfiguration()).thenReturn(config);
        when(newsReaderFactory.getReader(anyString())).thenReturn(reader);
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
    public void loadingNewsWithTwoNoneInformationsShouldStoreAllInPublicContent() {
        IncomingInfo info = new IncomingInfo("1", SubsciptionType.NONE);
        IncomingInfo info2 = new IncomingInfo("2", SubsciptionType.NONE);
        IncomingNews news = new IncomingNews();
        news.add(info);
        news.add(info2);
        when(reader.read()).thenReturn(news);
        try {
            List<String> content = (List<String>) field.get(newsLoader.loadNews());
            assertThat(content.size(), is(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
