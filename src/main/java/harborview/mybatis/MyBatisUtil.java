package harborview.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MyBatisUtil {

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
                Logger.getLogger(MyBatisUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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
                Logger.getLogger(MyBatisUtil.class.getName()).log(Level.SEVERE, null, ex);
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
