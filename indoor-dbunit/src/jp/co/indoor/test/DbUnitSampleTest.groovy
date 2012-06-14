package jp.co.indoor.test;

import org.dbunit.Assertion;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import groovy.sql.Sql;
import groovy.util.GroovyTestCase;
import groovy.xml.StreamingMarkupBuilder;

class DbUnitSampleTest extends GroovyTestCase {

    static {
        Sql.newInstance('jdbc:mysql://localhost:3306/test', '', '', 'com.mysql.jdbc.Driver').executeUpdate('delete from emp')
    }
    
    JdbcDatabaseTester tester
    
    void setUp() {
        tester = new JdbcDatabaseTester('com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/test', '', '')
    }
    void test() {
        tester.dataSet = dataSet {
            emp empno:7369, ename:'SMITH', job:'CLERK'
            emp empno:7499, ename:'ALLEN', job:'SALESMAN'
            emp empno:7521, ename:'WARD', job:'SALESMAN'
        }
        
        tester.onSetup()

        assert 1 == Sql.newInstance('jdbc:mysql://localhost:3306/test', '', '', 'com.mysql.jdbc.Driver').executeUpdate('delete from emp where empno = 7499')

        Assertion.assertEquals dataSet {
            emp empno:7369, ename:'SMITH', job:'CLERK'
            emp empno:7521, ename:'WARD', job:'SALESMAN'
        }, tester.connection.createDataSet()
    }
    
    def dataSet(c) {
        new FlatXmlDataSet(new StringReader(new StreamingMarkupBuilder().bind{dataset c}.toString()))
    }
}
