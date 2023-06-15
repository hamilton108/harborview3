package harborview.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

//import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MyBatisUtil {

    private final Logger logger = LoggerFactory.getLogger(MyBatisUtil.class);
    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisUtil(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public <T> T withSession(Function<SqlSession,T> fn) {
        return withSession(fn, null);
    }
    public <T> T withSession(Function<SqlSession,T> fn, Consumer<Exception> errorHandler) {
        SqlSession session = null;
        try {
            session = getSession();
            return fn.apply(session);
        }
        catch (Exception ex) {
            if (errorHandler == null){
                logger.error(ex.getMessage());
                throw ex;
            }
            else {
                errorHandler.accept(ex);
            }
            return null;
        }
        finally {
            if (session != null) {
                session.commit();
                session.close();
            }
        }
    }

    /*
    public <T> T withSession2(Function<SqlSession,T> fn, Function<Exception,T> errHandler) {
        SqlSession session = null;
        try {
            session = getSession();
            return fn.apply(session);
        }
        catch (Exception ex) {
            return errHandler.apply(ex);
        }
        finally {
            if (session != null) {
                session.commit();
                session.close();
            }
        }
    }
     */

    public void withSessionConsumer(Consumer<SqlSession> consumer) {
        withSessionConsumer(consumer, null);
    }
    public void withSessionConsumer(Consumer<SqlSession> consumer, Consumer<Exception> errorHandler) {
        SqlSession session = null;
        try {
            session = getSession();
            consumer.accept(session);
        }
        catch (Exception ex) {
            if (errorHandler == null){
                logger.error(ex.getMessage());
                throw ex;
            }
            else {
                errorHandler.accept(ex);
            }
        }
        finally {
            if (session != null) {
                session.commit();
                session.close();
            }
        }
    }


}
