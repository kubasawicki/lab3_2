import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewsReaderFactory.class, ConfigurationLoader.class})
public class NewsLoaderTest {

    private ConfigurationLoader configLoader;
    private NewsLoader newsLoader;
    private Configuration config;
    private NewsReader reader;
    private IncomingNews incomingNews;

    @Before
    public void setup(){
        configLoader = PowerMockito.mock(ConfigurationLoader.class);
        config = new Configuration();
        newsLoader = new NewsLoader();
        reader = PowerMockito.mock(FileNewsReader.class);
        incomingNews = new IncomingNews();

        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);

        incomingNews.add(new IncomingInfo("incInfoA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("incInfoB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("incInfoC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("incInfoNONE", SubsciptionType.NONE));

        Whitebox.setInternalState(config, "readerType", config.getReaderType());

        PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(configLoader);
        PowerMockito.when(configLoader.loadConfiguration()).thenReturn(config);
        PowerMockito.when(NewsReaderFactory.getReader(config.getReaderType())).thenReturn(reader);
        PowerMockito.when(reader.read()).thenReturn(incomingNews);

    }
//    @Test
//    public void prepareForPublishShouldBeCalledOnlyOnce(){
//
//    }
}
