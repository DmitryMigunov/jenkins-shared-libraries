import TestData.BitbucketStatusTestData
import TestData.CommitIdTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_DefinedBuildStatus_ValidBitBucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_DefinedBuildStatus_ValidBitbucketStatuses()
    }

    protected String buildStatus
    protected String validBitbucketStatus

    BitbucketStatus_DefinedBuildStatus_ValidBitBucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.validBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageName', 'commitId')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', CommitIdTestData.lastCommitIdClosure)
    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_ValidBitBucketStatus_buildStateIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}
        def expectedStatus = "${validBitbucketStatus}".toString()

        bitbucketStatus_(validBitbucketStatus)

        assertEquals(expectedStatus, actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_ValidBitBucketStatus_commitIdIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('1111', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_ValidBitBucketStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_EmptyRepoSlug_ValidBitBucketStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus, '')

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_NullRepoSlug_ValidBitBucketStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus, null)

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_WhitespaceRepoSlug_ValidBitBucketStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus, ' ')

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }


    @Test
    void test_BitbucketStatus_ValidBitBucketStatus_CustomRepoSlug_ValidBitBucketStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_(validBitbucketStatus, 'another_repoSlug')

        assertEquals('another_repoSlug', actualParameters['repoSlug'])

    }

}
