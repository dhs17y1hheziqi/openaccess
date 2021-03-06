
/*
 * Copyright (c) 1998 - 2005 Versant Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Versant Corporation - initial API and implementation
 */
package com.versant.core.jdo.junit.teststoredproc;

import com.versant.core.jdbc.conn.PooledConnection;
import com.versant.core.jdo.VersantPersistenceManagerFactory;
import com.versant.core.jdo.junit.VersantTestCase;
import com.versant.core.jdo.junit.teststoredproc.model.*;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.JDOUserException;
import java.sql.*;
import java.util.*;

/**
 * Test cases for stored procs
 */
public class TestStoredProcs extends VersantTestCase {

    /**
     */
    public void testfindPotentialReactionDuplicates2() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL", "{call OPN_FIRSTLEVELMETABOLICREGIONS(?)}");
        q.setClass(Metabolicregion.class);
        q.declareParameters("OUT.CURSOR p1");
        List result = (List) q.execute(null);

        Iterator it = result.iterator();
        while (it.hasNext()) {
            System.out.println("it.next() = " + it.next());
        }
        pm.close();
    }

    /**
     */
    public void testfindPotentialReactionDuplicates() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        //The reaction abbr and the id should match.
        String rxnAbbr = "LDH_L";
        Long rxnID = new Long(135631);

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();

        Query q = pm.newQuery("SQL",
                "{call GCS_REACTIONS.OPN_REACTIONDUPLICATES(?,?)}");
        q.declareParameters("NUMERIC p1, OUT.CURSOR p2");
        q.setClass(Reaction.class);
        List result = (List) q.execute(rxnID, null);
        assertTrue("Couldn't find any", result.size() > 0);

        Iterator it = result.iterator();
        while (it.hasNext()) {
            Reaction dup = (Reaction) it.next();
            System.out.println("      " + dup.getAbbreviation());
        }
        pm.currentTransaction().rollback();
        pm.close();
    }

    public void testfindFirstLevelMetabolicregions() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();

        Query q = pm.newQuery("SQL",
                "{call OPN_FIRSTLEVELMETABOLICREGIONS(?)}");
        q.setClass(Metabolicregion.class);
        q.declareParameters("OUT.CURSOR p1");
        List result = (List) q.execute(null);

        //TODO: inflate a list of Metabolicregaion objects:
        //And add to result vector.
        assertTrue("Failed to find first level regions.", result.size() > 0);
        for (int i = 0; i < result.size(); i++) {
            Metabolicregion oneObject = (Metabolicregion) result.get(i);
            System.out.println("      " + oneObject.getName());
            assertTrue(oneObject.getParentRegion() == null);
        }
        pm.currentTransaction().rollback();
        pm.close();
    }

    /**
     * Test Find Reactions.
     */
    public void testFindReactionsByMetaboliteCriteria() {
        if (true) {
            broken();
            return;
        }

        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String metaboliteAbbr = "occ";
        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();

        Query q = pm.newQuery("SQL",
                "{call OPN_REACTIONSBYMETABOLITECRIT(?, ?, ?, ?, ?, ?, ?)}");
        q.setClass(Reaction.class);
        q.declareParameters("VARCHAR p1, VARCHAR p2, VARCHAR p3, VARCHAR p4, VARCHAR p5, VARCHAR p6, OUT.CURSOR p7");
        List result = (List) q.executeWithArray(new Object[]{metaboliteAbbr, "", null, "N", null, "N", null});

//    		CallableStatement theStatement = getConnection().prepareCall("{ call OPN_REACTIONSBYMETABOLITECRIT(?, ?, ?, ?, ?, ?, ?) }");
//    		theStatement.setString(1, metaboliteAbbr); //Metabolite Abbreviation
//    		theStatement.setString(2, ""); //Metabolite Official Name
//    		theStatement.setString(3, null); //Reactant Role
//    		theStatement.setString(4, "N"); //Case Sensitive?
//    		theStatement.setString(5, null); //Compartment
//    		theStatement.setString(6, "N"); //exact match?
//    		theStatement.registerOutParameter(7, OracleTypes.CURSOR);
//    		theStatement.execute();
//    		ResultSet theResultSet = (ResultSet) theStatement.getObject(7);
//    		while (theResultSet != null && theResultSet.next()) {
//            //TODO: get A list of reactions and add to result Vector.
//			}

        assertTrue("Couldn't find any", result.size() > 0);
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            Reaction rxn = (Reaction) iter.next();
            System.out.println("      " + rxn.getAbbreviation());
            //assertTrue(c.getAbbreviation().indexOf(searchTerm) > 0);
        }

        pm.close();
    }

    /**
     * Test Find command parameters.
     * Test return cursor inside a cursor.
     * <p/>
     * Creation date: (02/13/2001 18:14:56)
     */
    public void testFindCommandParameters() {
        if (true) {
            broken();
            return;
        }

        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        long oid = 139699; //"Add User" command.

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();

        Query q = pm.newQuery("SQL",
                "{call GCS_ADMINCONSOLE.getCommandParameters(?,?) }");
        q.setClass(GcsAdmincommandparameters.class);
        q.declareParameters("NUMERIC p1, OUT.CURSOR p2");
        List result = (List) q.execute(new Long(oid), null);
        for (int i = 0; i < result.size(); i++) {
            GcsAdmincommandparameters gcsAdmincommandparameters = (GcsAdmincommandparameters) result.get(i);
            System.out.println("gcsAdmincommandparameters = " + gcsAdmincommandparameters);
        }
        pm.close();

//			CallableStatement stmt =
//				getConnection().prepareCall("{ call GCS_ADMINCONSOLE.getCommandParameters(?,?) }");
//			stmt.setLong(1, oid);
//			stmt.registerOutParameter(2, OracleTypes.CURSOR);
//			stmt.execute();
//			ResultSet rset = (ResultSet) stmt.getObject(2);

//			while (true) {

        //TODO: get A list of CommandParameters and add to result Vector.
        /*CommandParameter parm = new CommandParameter(rset.getString(3)); // 3=type
        parm.setOid(rset.getLong(1));
        parm.setName(rset.getString(2));
        // String(3) is type, used in constructor above
        parm.setSize(rset.getFloat(4));
        parm.setSequenceNumber(rset.getInt(5));
        parm.setOptional(DatabaseUtilities.isDatabaseBooleanTrue(rset.getString(6)));
        // rset column 7 is a validation command, not used yet
        if (parm.isList()) {
            // getCursor is not on the standard JDBC ResultSet
            OracleResultSet oracleRset = (OracleResultSet) rset;
            ResultSet nestedRset = oracleRset.getCursor(8);
            List options = new ArrayList();
            while (nestedRset.next()) {
                options.add(nestedRset.getString(1));
            }
            parm.makeListItems(options);
        }
        result.add(parm);
        */
//			}

        assertTrue("Couldn't find any", result.size() > 0);
        Iterator iter = result.iterator();
    }

    /**
     * Test Find Reactions.
     * <p/>
     * Creation date: (02/13/2001 18:14:56)
     */
    public void testFindReactions() {
        if (true) {
            broken();
            return;
        }

        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String searchTerm = "pfk";

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL",
                "{ call OPN_REACTIONS(? , ? , ? , ? , ? ) }");
        q.setClass(Reaction.class);
        q.declareParameters("VARCHAR p1, VARCHAR p2, VARCHAR p3, VARCHAR p4, OUT.CURSOR p5");
        List result = (List) q.executeWithArray(new Object[]{searchTerm, "N", "N", "ABBREVIATION", null});

//			CallableStatement cs = getConnection().prepareCall("{ call OPN_REACTIONS(? , ? , ? , ? , ? ) }");
//            cs.setString(1, searchTerm); // SearchTerm
//            cs.setString(2, "N"); // Not search all field
//            cs.setString(3, "N"); //Case insensitive search
//            cs.setString(4, "ABBREVIATION"); // Search Abbr field
// 			cs.registerOutParameter(5, OracleTypes.CURSOR); // OUTPUT
//			cs.execute();
//			OracleResultSet rset = (OracleResultSet) cs.getObject(5);

        assertTrue("Couldn't find any", result.size() > 0);
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            Reaction rxn = (Reaction) iter.next();
            System.out.println("      " + rxn.getAbbreviation());
            //assertTrue(c.getAbbreviation().indexOf(searchTerm) > 0);
        }
        pm.close();
    }

    public void testFindCompounds() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String searchTerm = "cas";
        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL",
                "{ call OPN_COMPOUNDS(? , ?, ? , ? , ? ) }");
        q.setClass(Compound.class);
        q.declareParameters("VARCHAR p1, VARCHAR p2, VARCHAR p3, VARCHAR p4, OUT.CURSOR p5");
        List result = (List) q.executeWithArray(new Object[]{searchTerm, "N", "N", "ABBREVIATION", null});

        assertTrue("Couldn't find any", result.size() > 0);
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            Compound c = (Compound) iter.next();
            System.out.println("      " + c.getAbbreviation());
            //assertTrue(c.getAbbreviation().indexOf(searchTerm) > 0);
        }
        pm.currentTransaction().rollback();
        pm.close();
    }

    /**
     * The same as 'testFindCompounds' but without the candidate class.
     * This will not try and map to a managed instance.
     */
    public void testFindCompounds2() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String searchTerm = "cas";
        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL",
                "{ call OPN_COMPOUNDS(? , ?, ? , ? , ? ) }");
        q.declareParameters("VARCHAR p1, VARCHAR p2, VARCHAR p3, VARCHAR p4, OUT.CURSOR p5");
        List result = (List) q.executeWithArray(new Object[]{searchTerm, "N", "N", "ABBREVIATION", null});

        assertTrue("Couldn't find any", result.size() > 0);
        Iterator iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println("\nnext iter");
            Object[] c = (Object[]) iter.next();
            for (int i = 0; i < c.length; i++) {
                System.out.println("col[" + i + 1 + "] = " + c[i]);
            }
        }
        pm.currentTransaction().rollback();
        pm.close();
    }

    /**
     * Test the Reaction.isBalanced() and Reaction.imbalance() methods.
     */
    public void testReactionIsBalanced() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String abbreviation = "PFK";
        Long rxnID = new Long(134377);

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL",
                "{ call getReactionElementBalance(?,?,?) }");
//        q.setClass(Reaction.class);
        q.declareParameters("NUMERIC p1, OUT.CHAR p2, OUT.CURSOR p3");
        Object[] result = (Object[]) q.execute(rxnID, null, null);

        String isBalancedString = (String) result[0];
        System.out.println("isBalancedString = " + isBalancedString);
        boolean unbalanced = (isBalancedString == null || !isBalancedString
                .startsWith("Y"));
        assertTrue(unbalanced);

//        Object[] ubbalanced = (Object[]) result[1];
//        for (int i = 0; i < ubbalanced.length; i++) {
//            Object[] row = (Object[]) ubbalanced[i];
//            for (int j = 0; j < row.length; j++) {
//                Object o = row[j];
//                System.out.println("o = " + o);
//            }
//        }

        Collection unbalancedElements = (Collection) result[1];
        for (Iterator iterator = unbalancedElements.iterator();
             iterator.hasNext();) {
            Object[] elData = (Object[]) iterator.next();
            String element = (String) elData[0];
            Number sum = (Number) elData[1];
            System.out.println("element: " + element + " sum: " + sum);
        }
        pm.currentTransaction().rollback();
        pm.close();
    }

    public void testReactionIsBalanced2() {
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        String abbreviation = "PFK";
        Long rxnID = new Long(134377);

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL",
                "{ call getReactionElementBalance(?,?,?) }");
        q.setClass(Reaction.class);
        q.declareParameters("NUMERIC p1, OUT.CHAR p2, OUT.CURSOR p3");
        try {
            Object[] result = (Object[]) q.execute(rxnID, null, null);
            fail("expected userException");
        } catch (JDOUserException e) {
            e.printStackTrace();
        }
        pm.close();
    }

    public void testValidateReactionNumber() {
        if (true) {
            broken();
            return;
        }
        
        if (!getDbName().equals("oracle")) {
            unsupported();
            return;
        }

        System.out.println("\n\n TestStoredProcs.testValidateReactionNumber");

        Set reactionNumbers = new HashSet();
        Set invalidRxnNumbers = new HashSet();

        // RxnMap: key is the rxnNumber, value is the Rxn OID.
        // RxnDirectionMap: key is the rxnNumber, value is the Rxn Direction.
        Map rxnMap = new HashMap();
        Map rxnDirectionMap = new HashMap();

        reactionNumbers.add(new Long(2));
        reactionNumbers.add(new Long(-4));
        reactionNumbers.add(new Long(40));
        reactionNumbers.add(new Long(30));

        Vector result = new Vector();
        try {

            Connection con = getConnection();
            Array rxnNumbersTab = getOidsARRAY(reactionNumbers, con);
            CallableStatement cs = getConnection().prepareCall("{ call validateReactionNumbers(?,?,?,?)}");
            cs.setArray(1, rxnNumbersTab); // A table of rxn numbers to be
            cs.registerOutParameter(2, Types.VARCHAR); // boolean, no reaction
            cs.registerOutParameter(3, OracleTypes.CURSOR);
            cs.registerOutParameter(4, OracleTypes.ARRAY); //this throws an exception
            cs.execute();

            //Any reaction found?
            boolean noReactionFound = "Y".equalsIgnoreCase(cs.getString(2));

            assertTrue(!noReactionFound);

            OracleResultSet rset = (OracleResultSet) cs.getObject(3);
            while (rset != null && rset.next()) {
                Long rxnID = null;
                if (rset.getLong(1) != 0) {
                    rxnID = new Long(rset.getLong(1)); //Reaction OID
                    //TODO: inflate the reaction based on the oid:
                    // and add to result vector.
                }

                Long rxnNumber = new Long(rset.getLong(2)); // Reaction Number
                String rxnDirection = rset.getString(3); // Reaction Direction
                // rxn oid is null if the rxn number is not found in DB.
                if (rxnID != null) {
                    rxnMap.put(rxnNumber, rxnID);
                    rxnDirectionMap.put(rxnNumber, rxnDirection);
                } else {
                    invalidRxnNumbers.add(rxnNumber);
                }

            }
            rset.close();
            cs.close();

            assertTrue("has invalidRxnNumbers", invalidRxnNumbers.size() > 0);
            assertTrue("has rxnMap", rxnMap.size() > 0);
            assertTrue("inflated Valid rxn", result.size() > 0);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception received in: " + ex.getMessage());
        } catch (Throwable th) {
            th.printStackTrace();
            fail("Throwable received in: " + th.getMessage());
        }
    }

    /**
     * Returns an ARRAY of BigDecimals.
     */
    private ARRAY getOidsARRAY(Set pOIDs, Connection conn) throws SQLException {
        if (pOIDs == null || pOIDs.isEmpty()) {
            return null;
        }

        //TODO: verify schema...
//        String schema = "Versant";
        String schema = "SYSTEM";
        StructDescriptor oidObjTyp = StructDescriptor.createDescriptor(schema + "." + "OIDOBJTYP", conn);

        STRUCT oIDs[] = new STRUCT[pOIDs.size()];

        Iterator it = pOIDs.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object[] aID = new Object[1];
            aID[0] = it.next();
            oIDs[i] = new STRUCT(oidObjTyp, conn, aID);

            i++;
        }
        ArrayDescriptor bigDecimalArray = ArrayDescriptor.createDescriptor(schema + "." + "OIDOBJTAB", conn);
        ARRAY oIdARRAY = new ARRAY(bigDecimalArray, conn, oIDs);
        return oIdARRAY;
    }

//    public static void main(String[] args) {
//        try {
//            Properties p = loadProperties();
//            TestStoredProcs test = new TestStoredProcs();
//            test.pmf = (VersantPersistenceManagerFactory)JDOHelper.getPersistenceManagerFactory(
//                    p);
//
//            test.testfindPotentialReactionDuplicates();       //working
//            test.testfindFirstLevelMetabolicregions();        //working
////            test.testFindReactionsByMetaboliteCriteria();
////            test.testFindCommandParameters();
////            test.testFindReactions();
//            test.testFindCompounds();                         //working
//            test.testFindCompounds2();                        //working
//            test.testReactionIsBalanced();                    //working
////            test.testValidateReactionNumber();
//            System.exit(0);
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//            System.exit(1);
//        }
//    }

    private Connection getConnection() throws SQLException {
        return ((PooledConnection) ((VersantPersistenceManagerFactory) pmf()).getJdbcConnection(null)).getCon();
    }

//    /**
//     */
//    public void testOracle() throws Exception {
////            CallableStatement cstmt = con.prepareCall("{? = call pkg_refcur.f_refcur(?)}");
////            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
////            cstmt.setString(2, i_etype);
////            cstmt.executeUpdate();
////            ResultSet rs = (ResultSet) cstmt.getObject(1);
//////            rset = ((OracleCallableStatement)((LoggingCallableStatement)cstmt).cstat).getCursor(1);
//
//        if (!getDbName().equals("oracle")) {
//            unsupported();
//            return;
//        }
//        PersistenceManager pm = pmf().getPersistenceManager();
//        pm.currentTransaction().begin();
//        for (int i = 0; i < 10; i++) {
//            Person p = new Person();
//            p.setName("name" + i);
//            p.setStreet("street" + i);
//            pm.makePersistent(p);
//        }
//        pm.currentTransaction().commit();
//
//
//        String i_etype = "SENIOR";
//        pm.currentTransaction().begin();
//        Query q = pm.newQuery("SQL", "{? = call pkg_refcur.f_refcur(?)}");
//        q.setClass(Person.class);
//        q.declareParameters("VARCHAR p1");
//        List result = (List) q.execute(i_etype);
//        for (int i = 0; i < result.size(); i++) {
//            Person person = (Person) result.get(i);
//        }
//        pm.close();
//    }
//
//    /**
//     * Test with includesubs
//     */
//    public void testOracle3() throws Exception {
////            CallableStatement cstmt = con.prepareCall("{? = call pkg_refcur.f_person_heirarchy()}");
////            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
////            cstmt.executeUpdate();
////            ResultSet rs = (ResultSet) cstmt.getObject(1);
//////            rset = ((OracleCallableStatement)((LoggingCallableStatement)cstmt).cstat).getCursor(1);
//
////        if (!getDbName().equals("oracle")) {
////            unsupported();
////            return;
////        }
//        PersistenceManager pm = pmf().getPersistenceManager();
//        pm.currentTransaction().begin();
//        for (int i = 0; i < 5; i++) {
//            Person p = new Person();
//            p.setName("name1-" + i);
//            p.setStreet("street" + i);
//            Address add = new Address();
//            add.setStreet("street-add-1" + i);
//            add.setTown("town-add-1" + i);
//            p.setAddress(add);
//            pm.makePersistent(p);
//        }
//        for (int i = 0; i < 5; i++) {
//            Person2 p = new Person2();
//            p.setName("name2-" + i);
//            p.setStreet("street" + i);
//            p.setName2("name2" + i);
//            Address add = new Address();
//            add.setStreet("street-add-2" + i);
//            add.setTown("town-add-2" + i);
//            p.setAddress(add);
//            pm.makePersistent(p);
//        }
//        for (int i = 0; i < 5; i++) {
//            Person3 p = new Person3();
//            p.setName("name3-" + i);
//            p.setStreet("street" + i);
//            p.setName3("name3" + i);
//            Address add = new Address();
//            add.setStreet("street-add-2" + i);
//            add.setTown("town-add-2" + i);
//            p.setAddress(add);
//            pm.makePersistent(p);
//        }
//        pm.currentTransaction().commit();
//
//
//        pm.currentTransaction().begin();
//        Query q = pm.newQuery("SQL", "{? = call pkg_refcur.f_person_heirarchy()}");
//        q.setClass(Person.class);
//        List result = (List) q.execute();
//        for (int i = 0; i < result.size(); i++) {
//            Person person = (Person) result.get(i);
//        }
//
//        Assert.assertEquals(15, result.size());
//        pm.close();
//    }
//

    public void testSybase() throws Exception {
        if (!getDbName().equals("sybase")) {
            unsupported();
            return;
        }
        createSP_AllPersons();

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.setName("name" + i);
            p.setStreet("street" + i);
            pm.makePersistent(p);
        }
        pm.currentTransaction().commit();

        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL", "{call allPersons}");
        q.setClass(Person.class);
        List result = (List) q.execute();
        for (int i = 0; i < result.size(); i++) {
            Person person = (Person) result.get(i);
        }
        assertEquals(10, result.size());
        pm.close();


        Connection con = pmf().getJdbcConnection(null);
        String createProcedure = "create procedure allPersons as " +
			    "SELECT a.nme, a.surname,a.street,b.street FROM person a " +
                "LEFT JOIN address b ON (a.person_id = b.address_id) " +
                "ORDER BY nme";
        CallableStatement cs = null;
        ResultSet rs = null;
        Statement stmt = null;
        con.setAutoCommit(false);
        con.createStatement().executeUpdate("sp_procxmode allPersons, anymode");

        cs = con.prepareCall("{call allPersons}");
        rs = cs.executeQuery();
        System.out.println("\n\n\n col 11 = " + rs.getMetaData().getColumnName(1));
        while (rs.next()) {
            System.out.println("rs.getString(1) = " + rs.getString(1));
        }
        if (con != null) con.close();
    }

    public void testSybase2() throws Exception {
        if (!getDbName().equals("sybase")) {
            unsupported();
            return;
        }
        createSP_1();

        PersistenceManager pm = pmf().getPersistenceManager();
        pm.currentTransaction().begin();
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.setName("name" + i);
            p.setStreet("street" + i);
            pm.makePersistent(p);
        }
        pm.currentTransaction().commit();

        pm.currentTransaction().begin();
        Query q = pm.newQuery("SQL", "{call SPPersonByName(?)}");
        q.declareParameters("VARCHAR p1");
        q.setClass(Person.class);
        List result = (List) q.execute("name" + 1);
        for (int i = 0; i < result.size(); i++) {
            Person person = (Person) result.get(i);
            System.out.println("person = " + person);
        }
        assertEquals(10, result.size());
        pm.close();


//        Connection con = ((PersistenceManagerFactoryBase)pmf()).getJdbcConnection(null);
//        CallableStatement cs = null;
//        ResultSet rs = null;
//        con.setAutoCommit(false);
//        con.createStatement().executeUpdate("sp_procxmode allPersons, anymode");
//
//        cs = con.prepareCall("{call allPersons(?)}");
//        rs = cs.executeQuery();
//        System.out.println("\n\n\n col 11 = " + rs.getMetaData().getColumnName(1));
//        while (rs.next()) {
//            System.out.println("rs.getString(1) = " + rs.getString(1));
//        }
//        if (con != null) con.close();
    }

    private void createSP_AllPersons() throws Exception {
        if (getDbName().equals("sybase")) {
            Connection con = pmf().getJdbcConnection(null);
            con.setAutoCommit(false);
            String deleteProcedure = "drop procedure allPersons";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deleteProcedure);
            con.commit();


            String createProcedure = "create procedure allPersons as " +
                    "SELECT a.person_id, a.nme, a.surname, a.street FROM person a " +
                    "ORDER BY nme";
            stmt = con.createStatement();
            stmt.executeUpdate(createProcedure);
            con.createStatement().executeUpdate("sp_procxmode allPersons, anymode");
            con.commit();
            con.close();
        }
    }

    private void createSP_1() throws Exception {
        System.out.println("TestStoredProcs.createSP_1");
        Connection con = pmf().getJdbcConnection(null);
        Statement stmt = null;
        try {
            con.setAutoCommit(false);
            String deleteProcedure = "drop procedure SPPersonByName";
            stmt = con.createStatement();
            stmt.executeUpdate(deleteProcedure);
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        con.rollback();

        String createProcedure = "create procedure SPPersonByName " +
                "@inv_id varchar(20) as select a.person_id, a.nme from " +
                "person a where a.nme = @inv_id";
        stmt = con.createStatement();
        stmt.executeUpdate(createProcedure);
        con.createStatement().executeUpdate("sp_procxmode SPPersonByName, anymode");
        con.commit();
        con.close();

    }

//
//    public void testSybase2() {
//        if (!getDbName().equals("sybase")) {
//            unsupported();
//            return;
//        }
//
//        PersistenceManager pm = pmf().getPersistenceManager();
//        pm.currentTransaction().begin();
//
//        Query q = pm.newQuery("sql", "allPersons");
//        q.setClass(Person.class);
//        Collection col = (Collection) q.execute();
//        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
//            Person person = (Person) iterator.next();
//            System.out.println("person = " + person);
//        }
//        pm.close();
//    }
//
//    public void testSybase3() throws Exception {
//        if (!getDbName().equals("sybase")) {
//            unsupported();
//            return;
//        }
//
//        PersistenceManager pm = pmf().getPersistenceManager();
//        pm.currentTransaction().begin();
//        for (int i = 0; i < 10; i++) {
//            Person p = new Person();
//            p.setName("name" + i);
//            p.setStreet("street" + i);
//            pm.makePersistent(p);
//        }
//        pm.currentTransaction().commit();
//
//        pm.currentTransaction().begin();
//        Query q = pm.newQuery("sql", "allPersons");
//        q.setClass(Person.class);
//        Collection col = (Collection) q.execute();
//
//        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
//            Person person = (Person) iterator.next();
//            System.out.println("person = " + person);
//        }
//        pm.close();
//    }

}
