package com.feko.generictabletoprpg.shared.features.basecontent

import com.feko.generictabletoprpg.shared.features.basecontent.domain.ILoadBaseContent
import com.feko.generictabletoprpg.shared.features.basecontent.domain.LoadBaseContentAdapter
import com.feko.generictabletoprpg.shared.features.basecontent.domain.usecase.ILoadBaseContentUseCase
import com.feko.generictabletoprpg.shared.features.basecontent.domain.usecase.LoadBaseContentUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val baseContentModule = module {
    singleOf(::LoadBaseContentAdapter) bind ILoadBaseContent::class
    singleOf(::LoadBaseContentUseCase) bind ILoadBaseContentUseCase::class
}