package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private IncomingNews incomingNews = new IncomingNews();
    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoaderMock;

    @Before public void setUp() throws Exception {
        mockStatic(ConfigurationLoader.class);
        configurationLoaderMock = PowerMockito.mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(new Configuration());

        NewsReader newsReaderMock = mock(NewsReader.class);
        when(newsReaderMock.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
        when(newsReaderFactory.getReader(null)).thenReturn(newsReaderMock);

        newsLoader = new NewsLoader();
    }

    @Test
    public void SplittingSubscribersAndFreeUsers_OnlyPublicNewsShouldBeLoaded(){
        IncomingInfo info1 = new IncomingInfo("test1", SubsciptionType.A);
        IncomingInfo info2 = new IncomingInfo("test2", SubsciptionType.B);
        IncomingInfo info3 = new IncomingInfo("test3", SubsciptionType.C);
        IncomingInfo infoNone = new IncomingInfo("public info", SubsciptionType.NONE);

        incomingNews.add(info1);
        incomingNews.add(info2);
        incomingNews.add(info3);
        incomingNews.add(infoNone);

        PublishableNews news = new PublishableNews();
        news.addPublicInfo("public info");
        news.addForSubscription("test1", SubsciptionType.A);
        news.addForSubscription("test2", SubsciptionType.B);
        news.addForSubscription("test3", SubsciptionType.C);

        assertThat(news, is(newsLoader.loadNews()));




    }

    @Test public void loadNews() throws Exception {
    }

}
