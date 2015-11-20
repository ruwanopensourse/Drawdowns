package lsf.drawdowns.dbCon;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Servlet implementation class IndexSrvlt
 */
@WebServlet(description = "this servelet will be the startup servlet and it "
		+ "may allow to open DB connection", urlPatterns = { "/IndexSrvlt" })
public class IndexSrvlt extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IndexSrvlt() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Gson gson = new Gson();
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		System.out.println("Drawdown system on live...!!!!!!1");
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Doget method running now........");

		String userPath = request.getServletPath();
		db_connections dbconnection = new db_connections();

		if (userPath.equals("/dataGet")) {

			try {
				System.out.println(request.getParameter("M"));

				String xx = "SELECT x.PERMNO_date AS PERMNO,x.CAPM_resid_date AS CAPM_resid_D FROM (SELECT PERMNO_date,YRMO_date,CAPM_resid_date FROM capm_drawdowns_date WHERE capm_drawdowns_date.HORIZON=1 AND YRMO_date='"
						+ request.getParameter("Q")
						+ request.getParameter("M")
						+ "') AS x , (SELECT PERMNO,YRMO,CAPM_resid FROM capm_drawdowns_results WHERE capm_drawdowns_results.HORIZON=1 AND YRMO='"
						+ request.getParameter("Q")
						+ request.getParameter("M")
						+ "') AS y WHERE x.PERMNO_date = y.PERMNO AND x.YRMO_date=y.YRMO ORDER BY y.CAPM_resid";
				ResultSet set = dbconnection.selectData(xx);

				JSONArray jsonarray = new JSONArray();
				while (set.next()) {
					JSONObject jsonobj = new JSONObject();
					int permno = set.getInt("PERMNO");
					String year_date = set.getString("CAPM_resid_D");
					if (year_date == null) {

					} else {
						try {
							jsonobj.put("permno", permno);
							jsonobj.put("capm_date", year_date);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						jsonarray.put(jsonobj);
					}
				}
				System.out.println();
				System.out.println(jsonarray);
				PrintWriter pwr = response.getWriter();
				pwr.print(jsonarray);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// dbconnection.con.close();
			}
		} else if (userPath.equals("/rangeData")) {

			try {
				
			//	String sql ="SELECT * FROM(SELECT v1.*, @counter := @counter +1 AS counter FROM (select @counter:=0) AS initvar, v1) AS X where counter <= (10/100 * @counter) AND YRMO BETWEEN 200401 AND 200412";
				
				String xx = "SELECT x.PERMNO_date AS PERMNO,x.CAPM_resid_date AS CAPM_resid_D FROM (SELECT PERMNO_date,YRMO_date,CAPM_resid_date,@counter := @counter +1 AS counter FROM (select @counter:=0) AS initvar,capm_drawdowns_date WHERE capm_drawdowns_date.HORIZON=1 AND YRMO_date='"
						+ request.getParameter("Q")
						+ request.getParameter("M")
						+ "') AS x , (SELECT PERMNO,YRMO,CAPM_resid FROM capm_drawdowns_results WHERE capm_drawdowns_results.HORIZON=1 AND YRMO='"
						+ request.getParameter("Q")
						+ request.getParameter("M")
						+ "') AS y WHERE counter <= (10/100 * @counter) AND  x.PERMNO_date = y.PERMNO AND x.YRMO_date=y.YRMO ORDER BY y.CAPM_resid";
				ResultSet set = dbconnection.selectData(xx);

				JSONArray jsonarray = new JSONArray();
				while (set.next()) {
					JSONObject jsonobj = new JSONObject();
					int permno = set.getInt("PERMNO");
					String year_date = set.getString("CAPM_resid_D");
					if (year_date == null) {

					} else {
						try {
							jsonobj.put("permno", permno);
							jsonobj.put("capm_date", year_date);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						jsonarray.put(jsonobj);
					}
				}
				System.out.println();
				System.out.println(jsonarray);
				PrintWriter pwr = response.getWriter();
				pwr.print(jsonarray);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// dbconnection.con.close();
			}

		} else if (userPath.equals("/summaryData")) {
			try {
				String sql = null;
				if (request.getParameter("D").equals("caff")) {
					sql = "SELECT YEAR(date_withyear) AS date,COUNT(YEAR(date_withyear)) AS count FROM caaf_drawdownend GROUP BY YEAR(date_withyear)";
				} else {
					sql = "SELECT YEAR(CAPM_resid_date) AS date,COUNT(YEAR(CAPM_resid_date)) AS count FROM capm_drawdowns_date GROUP BY YEAR(CAPM_resid_date)";
				}
				ResultSet set = dbconnection.selectData(sql);

				ArrayList<Integer> aryCount = new ArrayList<Integer>();
				ArrayList<Integer> aryYear = new ArrayList<Integer>();

				while (set.next()) {
					if (set.getInt("date") == 0) {

					} else {
						aryCount.add(set.getInt("count"));
						aryYear.add(set.getInt("date"));
					}
				}
				JSONObject obj = new JSONObject();
				obj.put("Total", aryCount);
				obj.put("year", aryYear);
				PrintWriter pwr = response.getWriter();
				pwr.print(obj);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (userPath.equals("/indexData")) {
			System.out.println("indexData method");
			try {
				// ResultSet set =
				// dbconnection.selectData("select Index_dates,Index_values from indexDrawdown where Year='"+
				// request.getParameter("Q") + "'");

				// ResultSet set =
				// dbconnection.selectData("SELECT mergedata.one AS Index_values,mergedata.one_d AS Index_dates FROM mergedata WHERE mergedata.permno = 0 AND mergedata.one_d LIKE '%"+
				// request.getParameter("Q") + "%'");
				ResultSet set = dbconnection
						.selectData("SELECT B.date_withyear AS Index_dates,A.value1 AS Index_values FROM ( SELECT  permno, value1,yrmo FROM caaf_drawdowns WHERE  permno=0 AND yrmo LIKE '"
								+ request.getParameter("Q")
								+ "%') AS  A  JOIN (SELECT  permno_end,date_withyear,yrmo_end FROM  caaf_drawdownend WHERE permno_end=0 AND yrmo_end LIKE '"
								+ request.getParameter("Q")
								+ "%') AS  B ON A.permno=B.permno_end AND A.yrmo=B.yrmo_end");
				ArrayList<Float> aryValue = new ArrayList<Float>();
				ArrayList<String> aryDate = new ArrayList<String>();
				while (set.next()) {
					aryValue.add(set.getFloat("Index_values"));
					aryDate.add(set.getString("Index_dates"));
				}
				JSONObject obj = new JSONObject();
				obj.put("value", aryValue);
				obj.put("date", aryDate);
				PrintWriter pwr = response.getWriter();
				pwr.print(obj);
				System.out.println(obj);
			} catch (SQLException | JSONException e) {
				e.printStackTrace();
			}
		} else if (userPath.equals("/index")) {
			/*
			 * try { ResultSet set = dbconnection.selectData(
			 * "SELECT B.date_withyear AS Index_dates FROM ( SELECT  permno, value1,yrmo FROM caaf_drawdowns WHERE  permno=0 AND yrmo LIKE '"
			 * + request.getParameter("Q") +
			 * "%') AS  A  JOIN (SELECT  permno_end,date_withyear,yrmo_end FROM  caaf_drawdownend WHERE permno_end=0 AND yrmo_end LIKE '"
			 * + request.getParameter("Q") +
			 * "%') AS  B ON A.permno=B.permno_end AND A.yrmo=B.yrmo_end");
			 * ArrayList<String> aryDate = new ArrayList<String>(); JSONArray
			 * jsonarray = new JSONArray(); while(set.next()){ JSONObject obj =
			 * new JSONObject();
			 * 
			 * obj.put("value", set.getString("Index_dates"));
			 * jsonarray.put(set.getString("Index_dates"));
			 * //jsonarray.put(obj); } PrintWriter pwr = response.getWriter();
			 * pwr.print(jsonarray); } catch (Exception e) { // TODO: handle
			 * exception }
			 */
			/*
			 * try {
			 * 
			 * ResultSet set = dbconnection .selectData(
			 * "select one_d from v_index_drawdown_dates where one_d like '%" +
			 * request.getParameter("Q") + "%'"); JSONArray jsonarray = new
			 * JSONArray(); while (set.next()) { JSONObject jsonobj = new
			 * JSONObject(); jsonobj.put("value", set.getString("one_d"));
			 * jsonarray.put(set.getString("one_d")); } PrintWriter pwr =
			 * response.getWriter(); pwr.print(jsonarray);
			 * System.out.println(jsonarray); } catch (SQLException e) {
			 * e.printStackTrace(); }
			 */
		} else if (userPath.equals("/test_getSet")) {
			PrintWriter pwr = response.getWriter();

					
			SessionFactory SFact = new Configuration().configure().buildSessionFactory();
			Session session = SFact.openSession();
			session.beginTransaction();
			
			//String all_drawdown = "SELECT x.PERMNO AS permno,x.YRMO AS yrmo,x.CAPM_resid AS drawdownValue,x.CAPM_resid_date AS drawdownDate,y.value1 AS marketCapitalization FROM (SELECT A.PERMNO,A.YRMO,A.CAPM_resid, B.PERMNO_date,B.YRMO_date,B.CAPM_resid_date FROM (SELECT * FROM capm_drawdowns_results WHERE YRMO LIKE '2004%' AND HORIZON =1) AS A INNER JOIN (SELECT * FROM capm_drawdowns_date WHERE YRMO_date LIKE '2004%' AND HORIZON=1) AS B ON A.PERMNO=B.PERMNO_date) AS x INNER JOIN (SELECT permno,yrmo,value1 FROM caaf_marketcapitalization WHERE yrmo LIKE '2004%') AS y ON y.permno=x.PERMNO AND y.yrmo=x.yrmo";
			String all_drawdown ="SELECT x.PERMNO AS permno,x.YRMO AS yrmo,x.CAPM_resid AS drawdownValue,y.CAPM_resid_date AS drawdownDate,x.value1 AS marketCapitalization,y.returnValue FROM ( SELECT A.PERMNO, A.YRMO, A.CAPM_resid, B.value1 FROM ( SELECT * FROM capm_drawdowns_results WHERE YRMO LIKE '2004%' AND HORIZON = 1) AS A INNER JOIN ( SELECT permno, yrmo, value1 FROM caaf_marketcapitalization WHERE yrmo LIKE '2004%') AS B ON A.PERMNO = B.permno ) AS x INNER JOIN (SELECT K.PERMNO_date,K.YRMO_date,K.CAPM_resid_date,L.value1 AS returnValue FROM (SELECT PERMNO_date,YRMO_date,CAPM_resid_date FROM capm_drawdowns_date WHERE YRMO_date LIKE '2004%' AND HORIZON = 1) AS K INNER JOIN (SELECT permno,yrmo,value1 FROM caaf_returns WHERE yrmo LIKE '2004%') AS L ON K.PERMNO_date=L.permno AND K.YRMO_date=L.yrmo) AS y ON y.PERMNO_date = x.PERMNO AND y.YRMO_date = x.yrmo";
			SQLQuery q = session.createSQLQuery(all_drawdown);
			//q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			q.setResultTransformer(Transformers.aliasToBean(Drawdown.class));
			/*
			String sql = "SELECT B.date_withyear AS Index_dates,A.value1 AS Index_values FROM ( SELECT  permno, value1,yrmo FROM caaf_drawdowns WHERE  permno=0 AND yrmo LIKE '2004%') AS  A  JOIN (SELECT  permno_end,date_withyear,yrmo_end FROM  caaf_drawdownend WHERE permno_end=0 AND yrmo_end LIKE '2004%') AS  B ON A.permno=B.permno_end AND A.yrmo=B.yrmo_end";
			String[] index_date = new String[12];
			try {
				ResultSet rset = dbconnection.selectData(sql);
				int i=0;
				while(rset.next()){
					//System.out.println(rset.getString(2));
					System.out.println(rset.getString(1));
					index_date[i]=rset.getString(1);
					i++;				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < 12; i++) {
				try {
					Date d1 = (Date) df.parse(index_date[i]);
					Date d2 = (Date) df.parse(index_date[i]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			*/
			@SuppressWarnings("unchecked")
			List<Drawdown> results = q.list();
			
			/*
			Collections.sort(results,new Comparator<Drawdown>(){
				@Override
				public int compare(Drawdown d0,Drawdown d1){
					return d0.getDrawdownDate().compareTo(d1.getDrawdownDate());
				}
				});*/
			
			
			for (Iterator<Drawdown> iterator = results.iterator(); iterator.hasNext();) {
				Drawdown data = (Drawdown) iterator.next();
				pwr.println(data.getPermno()+" : "+data.getYrmo()+" : "+data.getDrawdownDate()+" : "+data.getMarketCapitalization()+" : "+data.getReturnValue());
			}
			
			
			
			/*@SuppressWarnings("unchecked")
			List<?> list = session.createQuery("FROM model.CapmDrawdownsResults E WHERE E.id =  ").list();
			
			for (Iterator<CapmDrawdownsResults> iterator = list.iterator(); iterator.hasNext();) {
				CapmDrawdownsResults data = (CapmDrawdownsResults) iterator.next();
				pwr.println(data.getId().getPermno()+" : "+data.getCapmResid());
			}*/
			
			session.getTransaction().commit();

		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}
}