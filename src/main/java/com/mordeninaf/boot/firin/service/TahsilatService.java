package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Borc;
import com.mordeninaf.boot.firin.model.Tahsilat;
import com.mordeninaf.boot.firin.repository.TahsilatRepository;
import com.mordeninaf.boot.firin.util.Parameters;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TahsilatService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TahsilatService.class);

    @Autowired
    private TahsilatRepository tahsilatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Tahsilat findById(Integer id) {
        Optional<Tahsilat> tahsilat = tahsilatRepository.findById(id);
        return tahsilat.orElse(null);
    }

    public List<Tahsilat> findAll() {
        return tahsilatRepository.findAll();
    }

    public Page<Tahsilat> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return tahsilatRepository.findAll(paging);
    }

    public Page<Tahsilat> findAllByCariId(Integer cariId, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return tahsilatRepository.findAllByCariId(cariId, paging);
    }

    public List<Tahsilat> findAllByCariId(Integer cariId) {
        return tahsilatRepository.findAllByCariId(cariId);
    }

    public Page<Tahsilat> findAllByTarih(String basTarihi, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return tahsilatRepository.findAllByTarihOdeme(basTarihi, paging);
    }

    public List<Tahsilat> findAllByCariIdAndTarih(Integer cariId, String basTarihi) {
        /* DEFAULT */
        if (cariId == 0 && basTarihi.equals(Parameters.KURULUS_TARIHI)) {
            return tahsilatRepository.findAll();
        } else if (cariId > 0  && basTarihi.equals(Parameters.KURULUS_TARIHI)) {
            return tahsilatRepository.findAllByCariId(cariId);
        } else {
            return tahsilatRepository.findAllByTarihOdeme(basTarihi);
        }
    }

    public Page<Tahsilat> findAllByCariIdAndTarihOdemeBetween(Integer cariId, String basTarihi, String bitisTarihi, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        if (cariId == 0) {
            return tahsilatRepository.findAllByTarihOdemeBetween(basTarihi, bitisTarihi, paging);
        } else {
            return tahsilatRepository.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi, paging);
        }
    }

    public List<Tahsilat> findAllByCariIdAndTarihOdemeBetween(Integer cariId, String basTarihi, String bitisTarihi) {
        if (cariId == 0) {
            return tahsilatRepository.findAllByTarihOdemeBetween(basTarihi, bitisTarihi);
        } else {
            return tahsilatRepository.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi);
        }
    }

    public List<Tahsilat> findAllByTarihOdemeBetween(String basTarihi, String bitisTarihi) {
        return tahsilatRepository.findAllByTarihOdemeBetween(basTarihi, bitisTarihi);
    }

    public Page<Tahsilat> findAllByTarihOdemeWithoutTahsil (Integer cariId, String basTarihi, Pageable paging) {
        String cariSql = "";
        if (cariId > 0)
            cariSql = " AND cari_id = ? ";
        cariSql = " ";

        final String countQuery = "SELECT count(*) FROM " +
                "(SELECT id, cari_id, tutar, tarih_odeme, tarih " +
                "FROM TAHSILAT WHERE tarih_odeme = ? " +
                "UNION " +
                "SELECT id, id as cari_id, 0 as tutar, null as tarih_odeme, null as tarih " +
                "FROM CARI WHERE id NOT IN (SELECT cari_id FROM TAHSILAT WHERE tarih_odeme = ?)" +
                ") ";

        final String tahsilQuery = "SELECT * FROM " +
                "(SELECT id, cari_id, tutar, tarih_odeme, tarih " +
                "FROM TAHSILAT WHERE tarih_odeme = ? " +
                "UNION " +
                "SELECT id, id as cari_id, 0 as tutar, null as tarih_odeme, null as tarih " +
                "FROM CARI WHERE id NOT IN (SELECT cari_id FROM TAHSILAT WHERE tarih_odeme = ?)" +
                ") X LIMIT ? OFFSET ?";

        Integer tahsilatCount = jdbcTemplate.queryForList(countQuery, Integer.class, basTarihi, basTarihi).get(0);
        List<Tahsilat> tahsilatList = jdbcTemplate.query(tahsilQuery,new TahsilatMapper(), basTarihi, basTarihi, paging.getPageSize(), paging.getOffset());
        return new PageImpl<>(tahsilatList, paging, tahsilatCount);
    }

    public Page<Borc> getPaginatedBorcList(List<Borc> borcList, Integer pageNo, int pageSize, String borcSortBy) {
        return new PageImpl<>(borcList, PageRequest.of(pageNo, pageSize, Sort.by(borcSortBy)), borcList.size());
    }

    public static class TahsilatMapper implements RowMapper<Tahsilat> {
        public Tahsilat mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tahsilat tahsilat = new Tahsilat();
            tahsilat.setId(rs.getInt("id"));
            tahsilat.setCariId(rs.getInt("cari_id"));
            tahsilat.setTutar(rs.getInt("tutar"));

            String odemeTarihi = rs.getString("tarih_odeme");
            if (odemeTarihi == null)
                tahsilat.setTarihOdeme(LocalDate.now(ZoneId.systemDefault()).toString());
            else
                tahsilat.setTarihOdeme(rs.getString("tarih_odeme"));

            Timestamp timestamp = rs.getTimestamp("tarih");
            if (timestamp == null)
                tahsilat.setTarih(LocalDateTime.now());
            else
                tahsilat.setTarih(timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            return tahsilat;
        }
    }

    public Tahsilat save(Tahsilat tahsilat) {
        return tahsilatRepository.save(tahsilat);
    }

    public String remove(Tahsilat tahsilat) {
        try {
            tahsilatRepository.delete(tahsilat);
            return "OK";
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
            return "NOK";
        }
    }
}
