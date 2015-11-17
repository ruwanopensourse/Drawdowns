package model;

// Generated Nov 16, 2015 2:20:09 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CaafMarketcapitalizationId generated by hbm2java
 */
@Embeddable
public class CaafMarketcapitalizationId implements java.io.Serializable {

	private int permno;
	private int yrmo;

	public CaafMarketcapitalizationId() {
	}

	public CaafMarketcapitalizationId(int permno, int yrmo) {
		this.permno = permno;
		this.yrmo = yrmo;
	}

	@Column(name = "permno", nullable = false)
	public int getPermno() {
		return this.permno;
	}

	public void setPermno(int permno) {
		this.permno = permno;
	}

	@Column(name = "yrmo", nullable = false)
	public int getYrmo() {
		return this.yrmo;
	}

	public void setYrmo(int yrmo) {
		this.yrmo = yrmo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CaafMarketcapitalizationId))
			return false;
		CaafMarketcapitalizationId castOther = (CaafMarketcapitalizationId) other;

		return (this.getPermno() == castOther.getPermno())
				&& (this.getYrmo() == castOther.getYrmo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPermno();
		result = 37 * result + this.getYrmo();
		return result;
	}

}
