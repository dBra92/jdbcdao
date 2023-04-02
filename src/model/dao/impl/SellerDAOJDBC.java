package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override // 281
	public Seller findById(Integer id) {
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

	private Seller instantiateDepartment(ResultSet rs, Department depa) throws SQLException {// Propagar a exceção
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

	@Override // 283 - Busca pelo departamento
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement psFB = null;
		ResultSet rsFB = null;
		try {
			psFB = conn.prepareStatement(
					"SELECT seller.*, department.Name AS DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.id " + "WHERE DepartmentId = ? " + " ORDER BY Name");
			psFB.setInt(1, department.getId()); // 0, 0 interrogação, valor da interrogação
			rsFB = psFB.executeQuery();
			List<Seller> listSeller = new ArrayList<>();

			Map<Integer, Department> mapDep = new HashMap<>();

			while (rsFB.next()) {
				Department dep = mapDep.get(rsFB.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rsFB);
					mapDep.put(rsFB.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateDepartment(rsFB, dep);
				listSeller.add(obj);

			}
			return listSeller;
		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeResultSet(rsFB);
			DB.closeStatement(psFB);
		}
	}

	@Override // 284 - Buscar todos os vendedores
	public List<Seller> findAll() {
		PreparedStatement psFA = null;
		ResultSet rsFA = null;
		try {
			psFA = conn.prepareStatement(
					"SELECT seller.*, department.Name AS DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.id " + "ORDER BY Name");

			rsFA = psFA.executeQuery();
			List<Seller> listSeller = new ArrayList<>();

			Map<Integer, Department> mapDep = new HashMap<>();

			while (rsFA.next()) {
				Department dep = mapDep.get(rsFA.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rsFA);
					mapDep.put(rsFA.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateDepartment(rsFA, dep);
				listSeller.add(obj);

			}
			return listSeller;
		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeResultSet(rsFA);
			DB.closeStatement(psFA);
		}
	}

	@Override // 286
	public void update(Seller obj) {
		PreparedStatement psU = null;

		try {
			psU = conn.prepareStatement("UPDATE seller SET "
					+ "Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ? ");

			psU.setString(1, obj.getName());
			psU.setString(2, obj.getEmail());
			psU.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			psU.setDouble(4, obj.getBaseSalary());
			psU.setInt(5, obj.getDepartment().getId());
			psU.setInt(6, obj.getId());

			psU.executeUpdate();

		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeStatement(psU);
		}

	}

	@Override // 285
	public void insert(Seller obj) {
		PreparedStatement psI = null;

		try {
			psI = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			psI.setString(1, obj.getName());
			psI.setString(2, obj.getEmail());
			psI.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			psI.setDouble(4, obj.getBaseSalary());
			psI.setInt(5, obj.getDepartment().getId());

			int rowsAffected = psI.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = psI.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeStatement(psI);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement psD = null;
		try {
			psD = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			psD.setInt(1, id);

			int rowsAffected = psD.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("This line is empty!");
			}
		} catch (SQLException se) {
			throw new DbException(se.getMessage());
		} finally {
			DB.closeStatement(psD);
		}

	}
}
