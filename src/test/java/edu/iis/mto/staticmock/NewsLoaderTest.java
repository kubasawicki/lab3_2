package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    IncomingNews incomingNews = new IncomingNews();
    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoaderMock;

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(new Configuration());

        NewsReader newsReaderMock = mock(NewsReader.class);
        when(newsReaderMock.read()).thenReturn(incomingNews);
        mockStatic(NewsReaderFactory.class);
        // why Mockito.anyString() or Mockito.any(String.class) does not work?
        when(NewsReaderFactory.getReader((String) Mockito.any())).thenReturn(newsReaderMock);

        newsLoader = new NewsLoader();
    }

    @Test
    public void loadNewsAddPublicInfoOnlyForNoneIncomingInfo() {
        incomingNews.add(new IncomingInfo("A", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("B", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("C", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("None", SubsciptionType.NONE));

        PublishableNews publishableNews = newsLoader.loadNews();

        List<String> publicContent = null;
        Class<?> classPublishableNews = publishableNews.getClass();
        try {
            Field publicContentField = classPublishableNews.getDeclaredField("publicContent");
            publicContentField.setAccessible(true);

            publicContent = (List<String>) publicContentField.get(publishableNews);
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        List<String> publicList = new ArrayList<>();
        publicList.add("None");
        assertThat(publicContent, is(publicList));
    }

    @Test
    public void loadNewsCallsLoadConfiguration() {
        newsLoader.loadNews();
        verify(configurationLoaderMock, times(1)).loadConfiguration();
    }

}