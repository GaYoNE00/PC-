package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dao_Test {
	public String allcheck(int mbid, int cpuid, int ssd, int ramid, int rcount, int pwrid) {
		String allresult = "";
		String cpurs = "";
		
		cpurs = cputest(mbid, cpuid);
		
		return allresult;
	}
	public String cputest(int mbid, int cpuid) {
		String cpuresult = "";
		int mbsk = 0;
		int cpusk = 1;
		
		String sql = "select pcpu_sock, pmb_sock from product_mb, product_cpu where product_mb.pmb_id='"+mbid+"' product_cpu.pcpu_id='"+cpuid+"'";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				mbsk = rs.getInt(1);
				cpusk = rs.getInt(0);
				if(mbsk == cpusk) {
					cpuresult = "CPU와 메인보드가 호환됩니다.[호환가능]";					
				}else {
					cpuresult = "CPU와 메인보드가 호환불가합니다..";					
				}
			}else {
				cpuresult = "CPU와 메인보드간 호환성을 체크하지 못하였습니다.[사유 데이터 부족].";				
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cpuresult;

	}
	public String ssdcheck(int mbid, int ssdid) {
		String ssdresult = "";
		int mbsk = 0;
		int ssdsk = 1;
		
		String sql = "select pmb_m2sock, pssd_dsock from product_mb, product_ssd where product_mb.pmb_id='"+mbid+"' product_ssd.pssd_id='"+ssdid+"'";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				mbsk = rs.getInt(0);
				ssdsk = rs.getInt(1);
				if(ssdsk == 2 && mbsk>=1) {
					ssdresult = "CPU와 메인보드가 호환됩니다.[호환가능]";					
				}else {
					ssdresult = "메인보드에 M.2포트가 없습니다! [호환 불가!]";					
				}
			}else {
				ssdresult = "SSD와 메인보드간 호환성을 체크하지 못하였습니다.[사유 데이터 부족].";				
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ssdresult;
	}
	public String ramcheck(int mbid, int ramid, int rcount) {
		String ramresult = "";
		int mbsk = 0;
		int ramsk = 1;
		int getrcount = 0;
		
		String sql = "select pmb_rsock, pmb_ramC, pram_rsock from product_mb, product_ram where product_mb.pmb_id='"+mbid+"' product_ram.pram_id='"+ramid+"'";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				mbsk = rs.getInt(0);
				getrcount = rs.getInt(1);
				ramsk = rs.getInt(2);
				
				if(mbsk == ramsk) {
					if(getrcount >= rcount) {
						ramresult = "ram과 메인보드가 호환됩니다[호환가능]";
					}else {
						ramresult = "ram과 메인보드가 호환되나, 램의 갯수가 너무 많습니다.[주의]";						
					}
				}else {
					ramresult = "메인보드와 램이 서로 호환불가합니다. [호환 불가!]";					
				}
			}else {
				ramresult = "Ram와 메인보드간 호환성을 체크하지 못하였습니다.[사유 데이터 부족].";				
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ramresult;
	}
	public String gpucheck(int cpuid, String gpuid) {
		String ramresult = "";
		int cpugpuis = 0;
		int intgpu;
		if(gpuid != null) {
			intgpu = Integer.parseInt(gpuid);
		}else {
			String sql = "select cpu_igpu from product_cpu where product_cpu.pcpu_id='"+cpuid+"'";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58/m_server", "yeongho", "887900");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if(rs.next()) {
					cpugpuis = rs.getInt(0);
					if(cpugpuis == 1) {
						ramresult = "GPU가 없으나 CPU에 내장그래픽 카드가 내장되어 있어 그래픽 출력이 가능합니다.[호환가능]";
					}else if(cpugpuis == 0) {
						ramresult = "CPU에 내장그래픽 카드가 없어 그래픽 출력이 불가능합니다. \n 그래픽카드를 선택해주세요[호환불가능]";
						
					}

				}else {
					ramresult = "SSD와 메인보드간 호환성을 체크하지 못하였습니다.[사유 데이터 부족].";				
				}
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		


		return ramresult;
	}
}
