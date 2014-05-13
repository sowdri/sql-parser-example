package com.example;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.StatementNode;
import com.foundationdb.sql.parser.Visitable;
import com.foundationdb.sql.parser.Visitor;

/**
 * Hello world!
 * 
 */
public class FoundationDbTest {

	public static void main(String[] args) throws StandardException {
		String statement1 = "SELECT SUBQRY.* FROM (SELECT COL1, COL2 FROM TABLEA) AS SUBQRY";
		String statement2 = "select distinct a, b from a,b";
		String statement3 = "select county, sum(pvconversions + pcconversions) as total_conversions," + //
				" sum(impressions) as total_impressions, (sum(pvconversions + pcconversions) / nullif(sum(impressions), 0) * 1000) as cvr from report_geo_region where geo_country = 'GB' and region = 'East Midlands' and advertiser_id = advertiser group by county";
		String statement4 = "select county, sum(pvconversions + pcconversions) as total_x," + //
				" sum(impressions) as total_impressions, (sum(pvconversions + pcconversions) / nullif(sum(impressions), 0) * 1000) as cvr from report_geo_region where geo_country = 'GB' and region = 'East Midlands' and advertiser_id = advertiser group by county";
		String statement5 = "select geo_region, impressions, clicks, pvconversions, pcconversions, spend from "
				+ "(select sum(impressions) as impressions, sum(clicks) as clicks, sum(pvconversions) as pvconversions, sum(pcconversions) as pcconversions, sum(buyer_spend) as spend, geo_region from report_geo_region where category = 'category' and subcategory in ('Cat_subcategory') and geo_country = 'geo_country' and dayserial_numeric >= 2008 and dayserial_numeric <= 2009 group by geo_region) as subquery "
				+ "where spend > 0 order by spend desc";

		String statement = "select geo_region, impressions, clicks, pvconversions, pcconversions, spend from "
				+ "(select sum(impressions) as impressions, sum(clicks) as clicks, sum(pvconversions) as pvconversions, sum(pcconversions) as pcconversions, sum(buyer_spend) as spend, geo_region from report_geo_region where category = 'category' and subcategory in ('Cat_subcategory') and geo_country = 'geo_country' and dayserial_numeric >= 2008 and dayserial_numeric <= 2009 group by geo_region) as subquery "
				+ "where spend > 0 order by spend desc limit 10";

		SQLParser parser = new SQLParser();
		StatementNode stmt = parser.parseStatement(statement);

		Visitor v = new Visitor() {

			@Override
			public boolean visitChildrenFirst(Visitable node) {
				if (node instanceof SelectNode)
					return true;
				return false;
			}

			@Override
			public Visitable visit(Visitable node) throws StandardException {
				if (node instanceof ResultColumn) {
					ResultColumn resultColumn = (ResultColumn) node;
					System.out.println(resultColumn.getName());
				}
				return null;
			}

			@Override
			public boolean stopTraversal() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean skipChildren(Visitable node) throws StandardException {
				if (node instanceof FromList) {
					return true;
				}
				return false;
			}
		};

		stmt.accept(v);
		// stmt.treePrint();
	}
}
