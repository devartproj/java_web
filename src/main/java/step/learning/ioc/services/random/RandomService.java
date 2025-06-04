package step.learning.ioc.services.random;

public interface RandomService {
    void seed( String iv ) ;  // init vector
    String randomHex( int charLength ) ;  // random string from hex chars
}
