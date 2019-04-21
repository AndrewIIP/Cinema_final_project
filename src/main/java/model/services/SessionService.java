package model.services;

import model.dao.DaoFactory;
import model.dao.MovieDao;
import model.dao.SessionDao;
import model.dao.exceptions.DAOException;
import model.entity.Movie;
import model.entity.Session;

import java.util.List;
import java.util.Locale;

public class SessionService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    public Session getSessionById(int id) throws DAOException {
        Session session = new Session();

        try (SessionDao dao = daoFactory.createSessionDao()) {
            session = dao.getEntityById(id);
        }
        return session;
    }

    public void removeSessionById(int id) {
        try (SessionDao dao = daoFactory.createSessionDao()) {
            dao.delete(id);
        } catch (DAOException e){
            //TODO LOG
        }
    }

    public void createSession(Session session) {
        try (SessionDao dao = daoFactory.createSessionDao()) {
            dao.create(session);
        } catch (DAOException e){
            //TODO LOG
        }
    }

    public void setDaoLocale(Locale locale) {
        daoFactory.setDaoLocale(locale);
    }
}
