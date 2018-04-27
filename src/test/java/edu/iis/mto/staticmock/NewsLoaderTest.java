package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static edu.iis.mto.staticmock.NewsReaderFactory.getReader;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
    @InjectMocks
    NewsLoader newsLoader;
    @Mock
    ConfigurationLoader configurationLoaderMock;
    @Mock
    NewsReaderFactory newsReaderFactoryMock;
    @Mock
    NewsReader newsReaderMock;

    IncomingNews incomingNews;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();

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

}