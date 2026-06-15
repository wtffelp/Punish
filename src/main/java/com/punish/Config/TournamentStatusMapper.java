package com.punish.Config;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.punish.Model.Enums.TournamentStatus;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class TournamentStatusMapper implements ColumnMapper<TournamentStatus> {
    @Override
    public TournamentStatus map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        return TournamentStatus.valueOf(rs.getString(columnNumber));
    }
}