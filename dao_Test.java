package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dao_Test {
	public String allcheck(int cpuid, int mbid, int ramid, int rcount, String gpuid, int ssdid, String hddid, int pwrid) {
		String allresult = "";
		int recpu = cpucheck(mbid,cpuid).indexOf("[호환가능]");
		int reram = ramcheck(mbid,ramid,rcount).indexOf("[호환가능]");
		int ressd = ssdcheck(mbid,ssdid).indexOf("[호환가능]");
		int regpu = gpucheck(cpuid, gpuid).indexOf("[호환가능]");
		int repwr = powercheck(cpuid, mbid, ramid, rcount, gpuid, ssdid, hddid, pwrid).indexOf("[호환가능]");
		int reram2 = ssdcheck(mbid,ssdid).indexOf("[주의]");
		int repwr2 = powercheck(cpuid, mbid, ramid, rcount, gpuid, ssdid, hddid, pwrid).indexOf("[주의]");
		
		if(recpu != -1 && reram != -1 && ressd != -1 && regpu != -1 && repwr != -1) {
			allresult = "호환성 문제 없음";
		}else {
			if(recpu != -1 && (reram == -1||reram2 == -1) && ressd != -1 && regpu != -1 && (repwr != -1 || repwr2 != -1)) {
				allresult = "호환성 문제 없으나 주의사항 있음";				
			}else {
				allresult = "호환성 문제 있음";
			}
		}

		return allresult;
	}

	public String cpucheck(int mbid, int cpuid) {
		String cpuresult = "";
		int mbsk = 0;
		int cpusk = 1;

		String sql = "select pcpu_sock, pmb_sock from product_mb, product_cpu where product_mb.pmb_id=" + mbid
				+ " and product_cpu.pcpu_id=" + cpuid + "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58:3308/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cpusk = rs.getInt(1);
				mbsk = rs.getInt(2);
				if (mbsk == cpusk) {
					cpuresult = "CPU와 메인보드가 호환됩니다.[호환가능]";
				} else {
					cpuresult = "CPU와 메인보드가 호환불가합니다.";
				}
			} else {
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

		String sql = "select pmb_m2sock, pssd_dsock from product_mb, product_ssd where product_mb.pmb_id='" + mbid
				+ "' and product_ssd.pssd_id='" + ssdid + "'";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58:3308/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				mbsk = rs.getInt(1);
				ssdsk = rs.getInt(2);
				if (ssdsk == 1) {
					ssdresult = "CPU와 메인보드가 호환됩니다.[호환가능]";
				} else if(ssdsk == 2 && mbsk >= 1){
					ssdresult = "CPU와 메인보드가 호환됩니다.[호환가능]";

				}else if(ssdsk == 2 && mbsk < 1) {
					ssdresult = "메인보드에 M.2포트가 없습니다![호환 불가!]";					
				}
			} else {
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

		String sql = "select pmb_rsock, pmb_ramC, pram_rsock from product_mb, product_ram where product_mb.pmb_id='"
				+ mbid + "' and product_ram.pram_id='" + ramid + "'";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58:3308/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				mbsk = rs.getInt(1);
				getrcount = rs.getInt(2);
				ramsk = rs.getInt(3);

				if (mbsk == ramsk) {
					if (getrcount >= rcount) {
						ramresult = "ram과 메인보드가 호환됩니다.[호환가능]";
					} else {
						ramresult = "ram과 메인보드가 호환되나, 램의 갯수가 너무 많습니다.[주의]";
					}
				} else {
					ramresult = "메인보드와 램이 서로 호환불가합니다.[호환불가]";
				}
			} else {
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
		if (gpuid != null) {
			ramresult = "GPU과 시스템이 호환됩니다[호환가능].";
		} else {
			String sql = "select pcpu_igpu from product_cpu where product_cpu.pcpu_id='" + cpuid + "'";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58:3308/m_server", "yeongho",
						"887900");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					cpugpuis = rs.getInt(1);
					if (cpugpuis == 1) {
						ramresult = "GPU가 없으나 CPU에 내장그래픽 카드가 내장되어 있어 그래픽 출력이 가능합니다.[호환가능]";
					} else if (cpugpuis == 0) {
						ramresult = "CPU에 내장그래픽 카드가 없어 그래픽 출력이 불가능합니다. \n 그래픽카드를 선택해주세요[호환불가]";
					}
				} else {
					ramresult = "SSD와 메인보드간 호환성을 체크하지 못하였습니다.[사유 데이터 부족].";
				}
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ramresult;
	}

	public String powercheck(int cpuid, int mbid, int ramid, int rcount, String gpuid, int ssdid, String hddid,  int pwrid) {
		String powerresult = "";

		int cpupwr = 0;
		int mbpwr = 0;
		int gpupwr = 0;
		int rampwr = 0;
		int ssdpwr = 0;
		int hddpwr = 0;
		int power = 0;
		int powersum = 0;

		String sql = "";
		if (gpuid == null && hddid == null) {
			sql = "select pcpu_pwr, pmb_pwr, pram_pwr, pssd_pwr, ppwr_realcap"
					+ " from product_cpu, product_mb, product_ram, product_ssd, product_pwr"
					+ " where product_cpu.pcpu_id='" + cpuid + "' and product_mb.pmb_id='" + mbid
					+ "' and product_ssd.pssd_id='" + ssdid + "'and product_pwr.ppwr_id='" + pwrid
					+ "'and product_ram.pram_id='" + ramid + "'";
		} else if (gpuid == null) {
			sql = "select pcpu_pwr, pmb_pwr, pram_pwr, pssd_pwr, phdd_pwr, ppwr_realcap"
					+ " from product_cpu, product_mb, product_ram, product_ssd, product_hdd, product_pwr"
					+ " where product_cpu.pcpu_id='" + cpuid + "' and product_mb.pmb_id='" + mbid
					+ "' and product_ram.pram_id='" + ramid + "' and product_ssd.pssd_id='" + ssdid
					+ "' and product_hdd.phdd_id='" + hddid + "' and product_pwr.ppwr_id='" + pwrid + "'";

		} else if (hddid == null) {
			sql = "select pcpu_pwr, pmb_pwr, pram_pwr, pgpu_pwr, pssd_pwr, ppwr_realcap"
					+ " from product_cpu, product_mb, product_ram, product_gpu, product_ssd, product_pwr"
					+ " where product_cpu.pcpu_id='" + cpuid + "' and product_mb.pmb_id='" + mbid
					+ "' and product_ram.pram_id='" + ramid + "' and product_gpu.pgpu_id='" + gpuid
					+ "' and product_ssd.pssd_id='" + ssdid + "' and product_pwr.ppwr_id='" + pwrid + "'";

		} else {
			sql = "select pcpu_pwr, pmb_pwr, pram_pwr, pgpu_pwr, pssd_pwr, phdd_pwr, ppwr_realcap"
					+ " from product_cpu, product_mb, product_ram, product_gpu, product_ssd, product_hdd, product_pwr"
					+ " where product_cpu.pcpu_id='" + cpuid + "' and product_mb.pmb_id='" + mbid
					+ "' and product_ram.pram_id='" + ramid + "' and product_gpu.pgpu_id='" + gpuid
					+ "' and product_ssd.pssd_id='" + ssdid + "' and product_hdd.phdd_id='" + hddid
					+ "' and product_pwr.ppwr_id='" + pwrid + "'";
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://118.46.199.58:3308/m_server", "yeongho", "887900");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cpupwr = rs.getInt(1);
				mbpwr = rs.getInt(2);
				rampwr = rs.getInt(3);
				if (gpuid == null && hddid == null) {
					ssdpwr = rs.getInt(4);
					power = rs.getInt(5);
				} else if (gpuid == null) {
					ssdpwr = rs.getInt(4);
					hddpwr = rs.getInt(5);
					power = rs.getInt(6);
				} else if (hddid == null) {
					gpupwr = rs.getInt(4);
					ssdpwr = rs.getInt(5);
					power = rs.getInt(6);
				} else {
					gpupwr = rs.getInt(4);
					ssdpwr = rs.getInt(5);
					hddpwr = rs.getInt(6);
					power = rs.getInt(7);
				}
				powersum = cpupwr + mbpwr + (rampwr * rcount) + gpupwr + ssdpwr + hddpwr;
				if (power >= powersum) {
					if (power-100 >= powersum) {
						powerresult = "파워 용량이 충분합니다.[호환가능]";
					}else if(power-50 >= powersum) {
						powerresult = "파워 용량이 여유있습니다.[호환가능]";						
					}else if(power-10 >= powersum) {
						powerresult = "파워 용량이 딱 맞습니다.[호환가능]";						
					}else {
						powerresult = "파워 용량이 아슬아슬합니다.[주의]";
					}
				} else {
					powerresult = "파워 용량이 부족합니다.[전원부족]";
				}
			} else {
				powerresult = "시스템 호환성을 체크하지 못하였습니다.[데이터 베이스 오류].";
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return powerresult;
	}
}
