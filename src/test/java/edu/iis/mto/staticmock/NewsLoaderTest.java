package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static edu.iis.mto.staticmock.NewsReaderFactory.getReader;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    @InjectMocks
    private NewsLoader newsLoader;
    @Mock
    private ConfigurationLoader configurationLoaderMock;
    @Mock
    private NewsReaderFactory newsReaderFactoryMock;
    @Mock
    private NewsReader newsReaderMock;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        IncomingNews incomingNews = new IncomingNews();

        IncomingInfo incomingPublicInfo = new IncomingInfo("Public Test", SubsciptionType.NONE);
        IncomingInfo incomingSubcriptionAInfo = new IncomingInfo("Sub Test", SubsciptionType.A);
        incomingNews.add(incomingPublicInfo);
        incomingNews.add(incomingSubcriptionAInfo);

        mockStatic(ConfigurationLoader.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(new Configuration());

        newsReaderMock = mock(NewsReader.class);
        when(newsReaderMock.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactoryMock = mock(NewsReaderFactory.class);
        when(getReader(null)).thenReturn(newsReaderMock);
    }

    @Test
    public void loadNewsShouldGetReaderTypeFromConfiguration() {
        newsLoader.loadNews();
        verify(configurationLoaderMock, times(1)).loadConfiguration();
    }

    @Test
    public void loadNewsShouldPublishPublicNews() {
        PublishableNews publishableNews = newsLoader.loadNews();

        assert (publishableNews.getPublicContent().contains("Public Test"));
    }

    @Test
    public void loadNewsShouldNotPublishSubscriptionNews() {
        PublishableNews publishableNews = newsLoader.loadNews();

        assert (!publishableNews.getPublicContent().contains("Sub Test"));
    }
}
