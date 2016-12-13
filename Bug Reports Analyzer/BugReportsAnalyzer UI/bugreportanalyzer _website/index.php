<?php
error_reporting(0);

$dbconn = pg_connect("host=ec2-54-83-47-88.compute-1.amazonaws.com dbname=dchseapvaviet6 user=gfkfgfldrnvxiy password=fsqt-6Q8TB5sbdQWvhx66jKz1n");


function print_output($query)
{
    $result = pg_query($query);

    echo "<table class='table'>\n";

    $i = pg_num_fields($result);

    echo "\t<tr>\n";
    for ($j = 0; $j < $i; $j++) {
        $fieldname = pg_field_name($result, $j);
        echo "\t\t<th>$fieldname</th>\n";
    }
    echo "\t</tr>";

    while ($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
        echo "\t<tr>\n";
        foreach ($line as $col_value) {
            echo "\t\t<td>$col_value</td>\n";
        }
        echo "\t</tr>\n";
    }
    echo "</table>\n";
}

?>
    <!DOCTYPE html>
    <html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Bug Reports Analyzer</title>

        <!-- Bootstrap Core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Theme CSS -->
        <link href="css/freelancer.min.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet"
              type="text/css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

    </head>

    <body id="page-top" class="index">

    <!-- Navigation -->
    <nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand" href="#page-top">Bug Reports Analyzer</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li class="page-scroll">
                        <a href="?">Metrics</a>
                    </li>
                    <li class="page-scroll">
                        <a href="?action=query">Query</a>
                    </li>
                    

                    <li class="page-scroll">
                        <a href="?action=setup">Set Up</a>
                    </li>

                    <li class="page-scroll">
                        <a href="?action=slides">Slides</a>
                    </li>

                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>

    <?php
    if (isset($_GET["action"]) && $_GET["action"] == "slides") {
        ?>
        <div style="margin-top: 15px">
        <iframe src="https://docs.google.com/presentation/d/1b5I-SIyN8BfDO3B7UGmvQTXF3-cOVuXtG73JfcOrlvo/embed?start=false&loop=false&delayms=3000" frameborder="0" width="1300" height="760" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>
        </div>
        <br><br><br><br>

            <?php }
        elseif (isset($_GET["action"]) && $_GET["action"] == "setup") {
            ?>
                                                                                                                                                                                                                                                                                         ?>
        <!-- Header -->
        <header >
            <div class="container">
                <div style="margin-top: -70px" class="row">
                    <div  class="col-lg-12">
                        <div class="intro-text">
                            <span style="font-size: 400%; class="name">Set Up</span>
                            <hr style="margin-bottom: -100px" class="star-light">
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="container">
            <div class="row">
                <div  class="col-lg-12">
                    <div class="intro-text">
                        <h3>Prerequisite tools</h3>
                        <br>
                        <h4>JavaParser Tool:</h4>
                        <ol>
                            <li>Download or clone the tool from <a href="https://github.com/mkaouer/SWEN77201.2161/tree/master/Bug%20Reports%20Analyzer/JavaParser"> here</a>.</li>
                            <li>Import the project to eclipse.</li>
                            <li>Change the path in the source code to the location of your Java project which you would like to parse.</li>
                            <li>Run the tool by clicking on the run button in Eclipse.</li>
                            <li>The results of the parsed project will be stored on your desktop as csv file.</li>
                        </ol>
                        <br>
                        <h4>GitHub bug reports extractor tool:</h4>
                        <ol>
                            <li>Download or clone the tool from  <a href="https://github.com/mkaouer/SWEN77201.2161/tree/master/Bug%20Reports%20Analyzer/BugsReportsExtractor"> here</a>.</li>
                            <li>Unzip the "BugsReportsExtractor"</li>
                            <li>Open terminal or command prompt.</li>
                            <li>Change the location to the tool location.</li>
                            <li>Type "pip install -r requirements.txt" to install all the packages that you need to run the tool.</li>
                            <li>Run the tool by typing python extract.py [GitHub User][GitHub Name].</li>
                            <li>The tool will generate three excel files and store them in the tool location.</li>
                        </ol>
                        <br>

                        <h4>Understand:</h4>
                        <ol>
                            <li>Download Understand tool from  <a href="https://scitools.com/trial-download-3/"> here</a>.</li>
                            <li>Import your Java project to Understand tool.</li>
                            <li>Select the quality metrics you would like to get results for and generate the results.</li>
                            <li>Understand tool will generate a csv files that contains all the quality metrics for selected project.</li>
                        </ol>

                        <p>After generating the files from the tools listed above, insert all the files into your database and then you can start query.</p>
                        <br>
                        <br><br><br>

                    </div>
                </div>
            </div>
        </div>

        <?php
    }
    elseif (isset($_GET["action"]) && $_GET["action"] == "query") {

        ?>

        <!-- Header -->
        <header>
            <div class="container">
                <div style="margin-top: -70px" class="row">
                    <div class="col-lg-6">
                        <div class="intro-text">
                            <span style="font-size: 200%;float:left; class="name">Query</span>
                            <br><br><br>
                            <!--                            <hr  style="font-size: 62px class="star-light">-->
                            <form method="post">
                                <?php
                                if (isset($_POST["query"])) {
                                    echo '<textarea placeholder="Write your own query.. " name="query" rows="5" class="form-control">' . $_POST["query"] . '</textarea>';
                                } else {
                                    echo '<textarea  placeholder="Write your own query.. " name="query" rows="5" class="form-control"></textarea>';
                                }
                                ?><br/>
                                <button style="margin-bottom: -100px" class="btn btn-large btn-primary btn-block" type="submit">Run</button>
                            </form>
                        </div>
                    </div>

                    <div class="col-lg-6">
                        <span style="font-size: 200%;float:left;" class="name">Tables</span>
                        <br><br> <br>
                        <table class="table">


                            <tbody>
                            <tr>
                                <td>bugreports</td>
                                <td>commits</td>
                                <td>events</td>
                            </tr>
                            <tr>
                                <td>signal_metrics</td>
                                <td>signalresultsparsecode</td>
                                <td> </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </header>
        <?php
        if (isset($_POST["query"])) {
            ?>
            <!-- Result Section -->
            <section id="Result">
                <div class="container">
                    <div style="margin-top: -70px" class="row">
                        <div class="col-lg-12 text-center">
                            <h4>Query Results</h4>
                        </div>
                    </div>
                    <div class="row">
                        <div style="height:500px;overflow:auto;" class="col-lg-12">
                            <?php
                            $result = pg_query($_POST["query"]);

                            if (!$result) {
                                echo "<br /><center><h1 style='color: red'>Query is invalid!</h1></center>";
                            } else {
                                echo "<table class='table'>\n";

                                $i = pg_num_fields($result);

                                echo "\t<tr>\n";
                                for ($j = 0; $j < $i; $j++) {
                                    $fieldname = pg_field_name($result, $j);
                                    echo "\t\t<th>$fieldname</th>\n";
                                }
                                echo "\t</tr>";

                                while ($line = pg_fetch_array($result, null, PGSQL_ASSOC)) {
                                    echo "\t<tr>\n";
                                    foreach ($line as $col_value) {
                                        echo "\t\t<td>$col_value</td>\n";
                                    }
                                    echo "\t</tr>\n";
                                }
                                echo "</table>\n";
                            }
                            ?>
                        </div>
                    </div>
                </div>
            </section>
            <?php
        }

    } else {

        ?>
        <!-- Header -->
        <header >
            <div class="container">
                <div style="margin-top: -70px" class="row">
                    <div  class="col-lg-12">
                        <div class="intro-text">
                            <span style="font-size: 400%; class="name">Metrics</span>
                            <hr style="margin-bottom: -100px" class="star-light">
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Result Section -->
        <section id="Result">
            <div class="container">
                <div style="margin-top: -70px" class="row">
                    <div class="col-lg-12">
                        <table class="table table-condensed" style="border-collapse:collapse;">
                            <thead>
                            </thead>
                            <tbody>

                            <tr data-toggle="collapse" data-target="#q4" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>How many bug reports are created and closed at the same time?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q4">

                                        <div class="row">
                                            <div style="height:300px;overflow:auto;" class="col-lg-12">

                                                <?php
                                                $query = "select id, title, created_at, closed_at
                                        from bugreports
                                        where created_at = closed_at;";

                                                print_output($query);
                                                ?>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q5" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Highest time frame taken to fix the bug?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q5">
                                        <?php
                                        $query = "select id, title, created_at, closed_at, max(bugreports.closed_at - bugreports.created_at) as datediff
                                            from bugreports
                                            group by id, title, created_at, closed_at
                                            order by datediff desc
                                            limit 1;";

                                        print_output($query);
                                        ?>

                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q6" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Minimum time frame taken to fix the bug?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q6">
                                        <?php
                                        $query = "select id, title, created_at, closed_at, min(bugreports.closed_at - bugreports.created_at) as datediff
                                        from bugreports
                                        group by id, title, created_at, closed_at
                                        order by datediff ASC
                                        limit 1;";

                                        print_output($query);
                                        ?>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q7" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Class name associated with a particular bug report?
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q7">
                                        <div class="row">
                                            <div style="height:300px;overflow:auto;" class="col-lg-12">
                                                <?php
                                                $query = " select bugreports.id, commits.filename, bugreports.created_at, bugreports.closed_at
                                        FROM bugreports, events, commits
                                        where bugreports.id = events.issue_id
                                        AND
                                        commits.event_id = events.id ";
                                                print_output($query);
                                                ?>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q8" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Recently changed class?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q8">
                                        <?php
                                        $query = "select bugreports.id, commits.filename,bugreports.created_at
                                                    from bugreports, commits
                                                    order by bugreports.closed_at desc
                                                    LIMIT 1;";

                                        print_output($query);
                                        ?>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q9" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Bugs pointing to more than one class?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q9">
                                        <div class="row">
                                            <div style="height:300px;overflow:auto;" class="col-lg-12">
                                                <?php
                                                $query = "select events.issue_id, commits.event_id, commits.filename
                                            from commits, events
                                            group by events.issue_id, commits.event_id,commits.filename limit 100;";
                                                print_output($query);
                                                ?>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q10" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Multiple bugs pointing towards same class?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q10">
                                        <div class="row">
                                            <div style="height:300px;overflow:auto;" class="col-lg-12">
                                                <?php
                                                $query = "select filename,   events.issue_id
                                            from commits,events
                                            GROUP BY  filename, events.issue_id
                                            HAVING count(filename) > 1 limit 100;";

                                                print_output($query);
                                                ?>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>

                            <tr data-toggle="collapse" data-target="#q1" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Class that has most number of bugs?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q1">
                                        <?php
                                        $query = "SELECT filename, COUNT(filename) AS Occurance
                                                    FROM commits
                                                    GROUP BY filename
                                                    ORDER   BY Occurance DESC
                                                    LIMIT 1;";

                                        print_output($query);
                                        ?>
                                    </div>
                                </td>
                            </tr>
                            <tr data-toggle="collapse" data-target="#q2" class="accordion-toggle"
                                style="background-color: #eeeeee;">
                                <td>
                                    <button class="btn btn-default btn-xs"><span
                                            class="glyphicon glyphicon-eye-open"></span></button>
                                </td>
                                <td>Class that has most coupling?</td>
                            </tr>
                            <tr>
                                <td colspan="2" class="hiddenRow">
                                    <div class="accordian-body collapse" id="q2">
                                        <?php
                                        $query = "Select \"Name\", count(\"CountClassCoupled\") As count
                                        From \"signal_metrics\"
                                        Where \"FileType\" = 'File'
                                        group by \"Name\"
                                        ORDER BY count DESC
                                        limit 1;";

                                        print_output($query);
                                        ?>
                                    </div>
                                </td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
        <?php


    }
    ?>

    <!-- Footer -->
<!--    class="navbar-fixed-bottom"-->
    <footer class="navbar-fixed-bottom" style="text-align: center">
        <div class="footer-below">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12" >
                        Copyright &copy; Team Two 2016
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
    <div class="scroll-top page-scroll hidden-sm hidden-xs hidden-lg hidden-md">
        <a class="btn btn-primary" href="#page-top">
            <i class="fa fa-chevron-up"></i>
        </a>
    </div>

    <!-- jQuery -->
    <script src="vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

    <!-- Contact Form JavaScript -->
    <script src="js/jqBootstrapValidation.js"></script>
    <script src="js/contact_me.js"></script>

    <!-- Theme JavaScript -->
    <script src="js/freelancer.min.js"></script>

    </body>
    </html>
<?php
pg_close($dbconn);
?>