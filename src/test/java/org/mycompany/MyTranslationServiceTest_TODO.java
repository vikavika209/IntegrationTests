package org.mycompany;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class MyTranslationServiceTest_TODO {

    @Mock
    private Translate googleTranslate;
    @Mock
    private Translation googleTranslation;

    /**
     * 1. Happy case test.
     * <p>
     * When MyTranslationService::translateWithGoogle method is called with any sentence and target language is equal to "ru",
     * googleTranslate dependency should be called and translation.getTranslatedText() returned.
     * No other interactions with googleTranslate dependency should be invoked apart from a single call to googleTranslate.translate().
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsRu_success() {
        MyTranslationService myTranslationService = new MyTranslationService(googleTranslate);

        String stringToTranslate = "New string";
        String language = "ru";
        String expectedString = "Новая строка";

        Mockito.when(googleTranslate.translate(eq(stringToTranslate), any())).thenReturn(googleTranslation);
        Mockito.when(googleTranslation.getTranslatedText()).thenReturn(expectedString);

        String realResult = myTranslationService.translateWithGoogle(stringToTranslate, language);

        assertEquals(expectedString, realResult);

        Mockito.verify(googleTranslate).translate(eq(stringToTranslate), any());
        Mockito.verifyNoMoreInteractions(googleTranslate);

        Mockito.verify(googleTranslation).getTranslatedText();
        Mockito.verifyNoMoreInteractions(googleTranslation);
    }

    /**
     * 2. Unhappy case test when target language is not supported.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is not equal to "ru",
     * `IllegalArgumentException` should be thrown. `googleTranslate` dependency should not be called at all.
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsNotRu_failure() {
        MyTranslationService myTranslationService = new MyTranslationService(googleTranslate);

        String stringToTranslate = "New string";
        String language = "en";

        assertThrows(IllegalArgumentException.class,
                () -> myTranslationService.translateWithGoogle(stringToTranslate, language));

        Mockito.verifyNoInteractions(googleTranslate);
    }

    /**
     * 3. Unhappy case test when Google Translate call throws exception.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called. When `googleTranslate` dependency throws exception, it should be
     * wrapped into `MyTranslationServiceException` and the latter should be thrown from our method.
     */
    @Test
    void translateWithGoogle_googleTranslateThrowsException_failure() {
        MyTranslationService myTranslationService = new MyTranslationService(googleTranslate);

        String stringToTranslate = "New string";
        String language = "ru";

        Mockito.when(googleTranslate.translate(eq(stringToTranslate), any())).thenThrow(new RuntimeException());

        assertThrows(
                MyTranslationServiceException.class,
                () -> myTranslationService.translateWithGoogle(stringToTranslate, language));

        Mockito.verify(googleTranslate).translate(eq(stringToTranslate), any());
        Mockito.verifyNoMoreInteractions(googleTranslate);

        Mockito.verifyNoInteractions(googleTranslation);
    }
}