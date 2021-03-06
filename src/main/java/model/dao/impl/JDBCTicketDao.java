package model.dao.impl;

import model.dao.AbstractDao;
import model.dao.TicketDao;
import model.dao.exceptions.DAOException;
import model.dao.mappers.DayMapper;
import model.dao.mappers.MovieMapper;
import model.dao.mappers.SessionMapper;
import model.dao.mappers.TicketMapper;
import model.entity.Day;
import model.entity.Movie;
import model.entity.Session;
import model.entity.Ticket;
import model.util.LogGen;
import org.apache.log4j.Logger;
import static model.util.LogMsg.*;

import java.sql.*;
import java.util.*;

public class JDBCTicketDao extends AbstractDao implements TicketDao {
    private Connection connection;
    private Logger log = LogGen.getInstance();

    public JDBCTicketDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Ticket> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ticket update(Ticket entity) throws DAOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ticket getEntityById(Integer id) throws DAOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer id) throws DAOException {
        String sqlQuery = "DELETE FROM `cinema`.`tickets` WHERE `id` = " + id;
        PreparedStatement prepStatement = null;

        Connection connection = this.connection;
        try {
            prepStatement = connection.prepareStatement(sqlQuery);
            log.debug(PREP_STAT_OPENED + " in TicketDao delete()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in TicketDao delete()");
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_DELETING, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        } finally {
            try {
                if (prepStatement != null)
                    prepStatement.close();
                log.debug(PREP_STAT_CLOSED + " in TicketDao delete()");
            } catch (SQLException e) {
                log.error(PREP_STAT_CANT_CLOSE, e);
            }
        }
    }

    @Override
    public void create(Ticket entity) throws DAOException {
        String sqlQuery = "INSERT INTO `cinema`.`tickets` (`place`, `user_id`, `showtime_id`) VALUES (?, ?, ?)";
        PreparedStatement prepStatement = null;

        Connection connection = this.connection;
        try {
            prepStatement = connection.prepareStatement(sqlQuery);
            prepStatement.setInt(1, entity.getPlace());
            prepStatement.setInt(2, entity.getUserID());
            prepStatement.setInt(3, entity.getSessionID());
            log.debug(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS + " in TicketDao create()");
            try {
                prepStatement.execute();
                log.debug(QUERY_EXECUTED + " in TicketDao create()");
            } catch (SQLIntegrityConstraintViolationException e) {
                log.error(CANT_CREATE_TICKET, e);
                throw new DAOException(e.getMessage(), e);
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_CREATE, e);
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        } finally {
            try {
                if (prepStatement != null)
                    prepStatement.close();
                log.debug(PREP_STAT_CLOSED + " in TicketDao create()");
            } catch (SQLException e) {
                log.error(PREP_STAT_CANT_CLOSE, e);
            }
        }
    }

    @Override
    public List<Ticket> getByUserId(int userId) {
        List<Ticket> ticketList = new LinkedList<>();

        MovieMapper movieMapper = new MovieMapper();
        SessionMapper sessionMapper = new SessionMapper();
        DayMapper dayMapper = new DayMapper();
        TicketMapper ticketMapper = new TicketMapper();

        Map<Integer, Movie> moviesMap = new HashMap<>();
        Map<Integer, Session> sessionsMap = new HashMap<>();
        Map<Integer, Day> daysMap = new HashMap<>();
        Map<Integer, Ticket> ticketMap = new HashMap<>();

        String sqlQuery = "SELECT t.id,\n" +
                "       t.place,\n" +
                "       t.user_id,\n" +
                "       t.showtime_id,\n" +
                "       s.id,\n" +
                "       s.time,\n" +
                "       s.day_id,\n" +
                "       s.movie_id,\n" +
                "       d.id,\n" +
                "       dt.day_name,\n" +
                "       dt.day_name_short,\n" +
                "       m.id,\n" +
                "       mt.movie_name,\n" +
                "       m.pic_url\n" +
                "FROM cinema.tickets AS t\n" +
                "            LEFT JOIN cinema.users AS u ON t.user_id = u.id\n" +
                "            LEFT JOIN cinema.sessions AS s ON t.showtime_id = s.id\n" +
                "            LEFT JOIN cinema.movies AS m ON s.movie_id = m.id\n" +
                "            LEFT JOIN cinema.days AS d ON d.id = s.day_id\n" +
                "            LEFT JOIN cinema.days_translate as dt on dt.day_id = d.id\n" +
                "            LEFT JOIN cinema.movies_translate as mt on mt.movie_id = m.id\n" +
                "            LEFT JOIN cinema.languages as l ON l.id = mt.lang_id && l.id = dt.lang_id\n" +
                "WHERE l.lang_tag = \'" + super.getLocale().toLanguageTag() + "\' && t.user_id = \'" + userId + "\'\n" +
                "ORDER BY d.id, s.time;";
        PreparedStatement prepStatement = null;
        ResultSet resultSet = null;
        Connection connection = this.connection;

        try {
            prepStatement = connection.prepareStatement(sqlQuery);
            log.debug(PREP_STAT_OPENED + " in TicketDao getByUserId()");

            try {
                resultSet = prepStatement.executeQuery();
                log.debug(QUERY_EXECUTED + " in TicketDao getByUserId()");

                while (resultSet.next()) {
                    Ticket ticket = ticketMapper.extractFromResultSet(resultSet, 1, 2, 3, 4);
                    Session session = sessionMapper.extractFromResultSet(resultSet, 5, 6, 7, 8);
                    Day day = dayMapper.extractFromResultSet(resultSet, 9, 10, 11);
                    Movie movie = movieMapper.extractFromResultSet(resultSet, 12, 13, 14);

                    ticket = ticketMapper.makeUnique(ticketMap, ticket);
                    day = dayMapper.makeUnique(daysMap, day);
                    movie = movieMapper.makeUnique(moviesMap, movie);

                    session.setDay(day);
                    session.setMovie(movie);
                    session = sessionMapper.makeUnique(sessionsMap, session);

                    ticket.setSession(session);
                    ticketList.add(ticket);
                }
            } catch (SQLException e) {
                log.error(SQL_EXCEPTION_WHILE_READING_FROM_DB, e);
            } finally {
                try {
                    if (resultSet != null)
                        resultSet.close();
                    log.debug(RESULT_SET_CLOSED + " in TicketDao getByUserId()");
                } catch (SQLException e) {
                    log.error(RESULT_SET_CANT_CLOSE, e);
                }
            }
        } catch (SQLException e) {
            log.error(EXCEPTION_IN_PREPARED_STATEMENT_PROCESS, e);
        } finally {
            try {
                if (prepStatement != null)
                    prepStatement.close();
                log.debug(PREP_STAT_CLOSED + " in TicketDao getByUserId()");
            } catch (SQLException e) {
                log.error(PREP_STAT_CANT_CLOSE, e);
            }
        }
        return ticketList;
    }

    @Override
    public void close() {
        try {
            connection.close();
            log.debug(CONNECTION_CLOSED);
        } catch (SQLException e) {
            log.error(CANT_CLOSE_CONNECTION, e);
        }
    }
}
