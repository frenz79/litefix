package fixtrading.testcases;

import com.litefix.models.dictionary.DefaultFix44Dictionary;
import com.litefix.models.session.ClientFixSessionConfig;

public abstract class AbstractTest {

	public ClientFixSessionConfig getSessionConfig() {
		return new ClientFixSessionConfig()
				.setSenderCompId( "senderCompId" )
				.setTargetCompId( "targetCompId" )
				.setHeartBtInt( 1 )
				.setResetSeqNumFlag('N')
				.addServer("localhost", 5178)
				.addServer("localhost", 5179)
				.setDictionary( DefaultFix44Dictionary.init() );
	}
	
}
