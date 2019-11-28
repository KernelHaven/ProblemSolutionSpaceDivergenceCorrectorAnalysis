# ProblemSolutionSpaceDivergenceCorrectorAnalysis
The Problem-Solution-Space (PSS) Divergence Corrector is an analysis plug-in, which provides (proposals for) corrections of unintended divergences between artifacts in the problem space and the solution space of a Software Product Line (SPL). This identification is based on a set of identified divergences as provided by the [Problem-Solution-Space Divergence Detector](https://github.com/KernelHaven/ProblemSolutionSpaceDivergenceDetectorAnalysis). Please refer to the website of that plug-in for more information on the provided divergences.

Currently, there exists a direct mapping between detected divergences and possible corrections based on the type of the divergence. For example, for the Undefined-Variable-Divergence the Undefined-Variable-Correction exists, which will be created for each divergence of this type. In general, this plug-in constructs the corrections based on the information of the divergence information objects provided by the [Problem-Solution-Space Divergence Detector](https://github.com/KernelHaven/ProblemSolutionSpaceDivergenceDetectorAnalysis). Hence, the current proposals for corrections are automatically customized to provide a rather precise description of how and where to apply changes that will correct the divergence. Fully automated corrections of divergences are future work.

## Tutorials
* [Basic Video Tutorial](https://www.youtube.com/watch?v=gpBT9wiDRhE)
* [Incremental Variant Slide Tutorial](https://github.com/KernelHaven/ProblemSolutionSpaceMapperAnalysis/blob/master/Tutorials/PSS-CE%20Incremental%20Tutorial.pdf)

## KernelHaven Setup
In order to retrieve (proposals for) corrections of unintended divergences, the analysis plug-in must be combined with the [Problem-Solution-Space Divergence Detector analysis plug-in](https://github.com/KernelHaven/ProblemSolutionSpaceDivergenceDetectorAnalysis), which in turn requires the [Problem-Solution-Space Mapper analysis plug-in](https://github.com/KernelHaven/ProblemSolutionSpaceMapperAnalysis). Further, this plug-in supports a non-incremental and an incremental analysis variant. The non-incremental variant analyzes a given SPL in its current state completely, while the incremental variant only analyzes the latest changes to it. A KernelHaven configuration file for executing the respective variant of this analysis plug-in should contain the following information.

### Non-Incremental Variant
```Properties
######################
#     Directories    #
######################
resource_dir = res/
output_dir = output/
plugins_dir = plugins/
cache_dir = cache/
archive = false
source_tree = <TODO: PATH_TO SPL>
arch = x86

##################
#     Logging    #
##################
log.dir = log/
log.console = true
log.file = true
log.level = INFO

################################
#     Code Model Parameters    #
################################
code.provider.timeout = 0
code.provider.cache.write = false
code.provider.cache.read = false
code.extractor.class =  net.ssehub.kernel_haven.undertaker.UndertakerExtractor
code.extractor.files = main
# Undertaker parses header and code files separately
code.extractor.file_regex = .*\.(c|h)
code.extractor.threads = 2
code.extractor.add_linux_source_include_dirs = false
code.extractor.parse_to_ast = false

################################
#    Build Model Parameters    #
################################
build.provider.timeout = 0
build.provider.cache.write = false
build.provider.cache.read = false
build.extractor.class = net.ssehub.kernel_haven.kbuildminer.KbuildMinerExtractor
build.extractor.top_folders = main

#######################################
#     Variability Model Parameters    #
#######################################
variability.provider.timeout = 0
variability.provider.cache.write = false
variability.provider.cache.read = false
variability.extractor.class = net.ssehub.kernel_haven.kconfigreader.KconfigReaderExtractor

##############################
#     Analysis Parameters    #
##############################
analysis.class = net.ssehub.kernel_haven.analysis.ConfiguredPipelineAnalysis
analysis.pipeline = net.ssehub.kernel_haven.pss_divergence_corrector.ProblemSolutionSpaceDivergenceCorrector(net.ssehub.kernel_haven.pss_divergence_detector.ProblemSolutionSpaceDivergenceDetector(net.ssehub.kernel_haven.pss_mapper.ProblemSolutionSpaceMapper(cmComponent(), bmComponent(), vmComponent())))
analysis.output.intermediate_results = ProblemSolutionSpaceDivergenceDetector, ProblemSolutionSpaceMapper
analysis.output.type = xlsx
analysis.pss_mapper.variable_regex = CONFIG_.*
```

### Incremental Variant
```Properties
######################
#     Directories    #
######################
resource_dir = res/
output_dir = output/
plugins_dir = plugins/
cache_dir = cache/
archive = false
source_tree = <TODO: PATH_TO_EMPTY_DIR>
arch = x86

##################
#     Logging    #
##################
log.dir = log/
log.console = true
log.file = true
log.level = INFO

################################
#     Code Model Parameters    #
################################
code.provider.timeout = 0
code.provider.cache.write = false
code.provider.cache.read = false
code.extractor.class = net.ssehub.kernel_haven.block_extractor.CodeBlockExtractor
code.extractor.file_regex = .*(\\.c|\\.h|\\.S)
code.extractor.fuzzy_parsing = true
code.extractor.add_pseudo_block = false

################################
#    Build Model Parameters    #
################################
build.provider.timeout = 0
build.provider.cache.write = false
build.provider.cache.read = false
build.extractor.class = net.ssehub.kernel_haven.kbuildminer.KbuildMinerExtractor

#######################################
#     Variability Model Parameters    #
#######################################
variability.provider.timeout = 0
variability.provider.cache.write = false
variability.provider.cache.read = false
variability.extractor.class = net.ssehub.kernel_haven.kconfigreader.KconfigReaderExtractor

##############################
#     Analysis Parameters    #
##############################
analysis.class = net.ssehub.kernel_haven.analysis.ConfiguredPipelineAnalysis
analysis.pipeline = net.ssehub.kernel_haven.pss_divergence_corrector.ProblemSolutionSpaceDivergenceCorrector(net.ssehub.kernel_haven.pss_divergence_detector.ProblemSolutionSpaceDivergenceDetector(net.ssehub.kernel_haven.pss_mapper.ProblemSolutionSpaceMapper(cmComponent(), bmComponent(), vmComponent())))
analysis.output.type = xlsx
analysis.pss_mapper.variable_regex = CONFIG_.*

# Incremental extension setup
preparation.class.0 = net.ssehub.kernel_haven.incremental.preparation.IncrementalPreparation
incremental.hybrid_cache.dir = <TODO: PATH_TO_EMPTY_DIR>
incremental.input.source_tree_diff = <TODO: PATH_TO_GIT_DIFF_FILE>
incremental.variability_change_analyzer.execute = true
incremental.variability_change_analyzer.class = net.ssehub.kernel_haven.incremental.diff.analyzer.ComAnAnalyzer
incremental.code.filter = net.ssehub.kernel_haven.incremental.preparation.filter.ChangeFilter
incremental.build.filter = net.ssehub.kernel_haven.incremental.preparation.filter.VariabilityChangeFilter
incremental.variability.filter = net.ssehub.kernel_haven.incremental.preparation.filter.VariabilityChangeFilter
cnf.solver = SAT4J
```

Please note that the PSS Divergence Corrector plug-in is currently under development and, hence, tested only with this particular configuration.

## Usage
The PSS Divergence Corrector can only be used as an analysis pipeline as it requires a set of [unintended divergences](https://github.com/KernelHaven/ProblemSolutionSpaceDivergenceDetectorAnalysis) (see KernelHaven Setup above). In such a setup, it will only provide a possibly empty set of (proposals for) corrections. This set may be empty either if the set of divergences is empty or if the type of divergence is unknown.

## License
This plug-in is licensed under the Apache License 2.0.

## Acknowledgments
This work is partially supported by the ITEA3 project [REVaMP2](http://www.revamp2-project.eu/), funded by the BMBF (German Ministry of Research and Education) under grant 01IS16042H.
