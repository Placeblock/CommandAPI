// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception;

import de.placeblock.commandapi.exception.type.Dynamic2CommandExceptionType;
import de.placeblock.commandapi.exception.type.DynamicCommandExceptionType;
import de.placeblock.commandapi.exception.type.SimpleCommandExceptionType;

public interface BuiltInExceptionProvider {

    Dynamic2CommandExceptionType integerTooLow();

    Dynamic2CommandExceptionType integerTooHigh();

    DynamicCommandExceptionType literalIncorrect();

    SimpleCommandExceptionType readerExpectedStartOfQuote();

    SimpleCommandExceptionType readerExpectedEndOfQuote();

    DynamicCommandExceptionType readerInvalidEscape();

    DynamicCommandExceptionType readerInvalidBool();

    DynamicCommandExceptionType readerInvalidInt();

    SimpleCommandExceptionType readerExpectedInt();

    DynamicCommandExceptionType readerInvalidLong();

    SimpleCommandExceptionType readerExpectedLong();

    DynamicCommandExceptionType readerInvalidDouble();

    SimpleCommandExceptionType readerExpectedDouble();

    DynamicCommandExceptionType readerInvalidFloat();

    SimpleCommandExceptionType readerExpectedFloat();

    SimpleCommandExceptionType readerExpectedBool();

    DynamicCommandExceptionType readerExpectedSymbol();

    SimpleCommandExceptionType dispatcherUnknownCommand();

    SimpleCommandExceptionType dispatcherUnknownArgument();

    DynamicCommandExceptionType dispatcherParseException();
}
