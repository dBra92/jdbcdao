package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import model.dao.SellerDao;

/* Não precisa fechar a conexão(conn) pq ele pode servir para mais de uma operação*/
public class SellerDAOJDBC implements SellerDao {

	// Dependencia com a conexão
	private Connection conn;

	// Forçar a injeção de dependência
	public SellerDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) { // 281
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id" + " wHERE seller.id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Department depa = instantiateDepartment(rs);
				Seller obj = instantiateDepartment(rs, depa);
				return obj;
			}
			return null;
		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateDepartment(ResultSet rs, Department depa) throws SQLException {//Propagar a exceção
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(depa);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department depa = new Department();
		depa.setId(rs.getInt("DepartmentId"));
		depa.setName(rs.getString("DepName"));
		return depa;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
