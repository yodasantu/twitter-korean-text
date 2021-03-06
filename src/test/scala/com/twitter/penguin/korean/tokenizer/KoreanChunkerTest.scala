/*
 * Twitter Korean Text - Scala library to process Korean text
 *
 * Copyright 2014 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.penguin.korean.tokenizer

import com.twitter.penguin.korean.TestBase
import com.twitter.penguin.korean.tokenizer.KoreanChunker._

class KoreanChunkerTest extends TestBase {

  test("getChunks should correctly split a string into Korean-sensitive chunks") {
    assert(
      getChunks("안녕? iphone6안녕? 세상아?").mkString("/")
        === "안녕/?/ /iphone/6/안녕/?/ /세상아/?"
    )

    assert(
      getChunks("This is an 한국어가 섞인 English tweet.").mkString("/")
        === "This/ /is/ /an/ /한국어가/ /섞인/ /English/ /tweet/."
    )

    assert(
      getChunks("이 日本것은 日本語Eng").mkString("/")
        === "이/ /日本/것은/ /日本語/Eng"
    )

    assert(
      getChunks("무효이며").mkString("/")
        === "무효이며"
    )

    assert(
      getChunks("#해쉬태그 이라는 것 #hash @hello 123 이런이런 #여자최애캐_5명으로_취향을_드러내자").mkString("/")
        === "#해쉬태그/ /이라는/ /것/ /#hash/ /@hello/ /123/ /이런이런/ /#여자최애캐_5명으로_취향을_드러내자"
    )
  }

  test("getChunkTokens should correctly find chunks with correct POS tags") {
    assert(
      chunk("한국어와 English와 1234와 pic.twitter.com " +
        "http://news.kukinews.com/article/view.asp?" +
        "page=1&gCode=soc&arcid=0008599913&code=41121111 " +
        "hohyonryu@twitter.com 갤럭시 S5").mkString("/")
        ===
      "한국어와(Korean: 0, 4)/ (Space: 4, 1)/English(Alpha: 5, 7)/와(Korean: 12, 1)/" +
        " (Space: 13, 1)/1234(Number: 14, 4)/와(Korean: 18, 1)/ (Space: 19, 1)/" +
        "pic.twitter.com(URL: 20, 15)/ (Space: 35, 1)/http://news.kukinews.com/" +
        "article/view.asp?page=1&gCode=soc&arcid=0008599913&code=41121111(URL: 36, 89)/" +
        " (Space: 125, 1)/hohyonryu@twitter.com(Email: 126, 21)/ (Space: 147, 1)/" +
        "갤럭시(Korean: 148, 3)/ (Space: 151, 1)/S(Alpha: 152, 1)/5(Number: 153, 1)"
    )

    assert(
      chunk("우와!!! 완전ㅋㅋㅋㅋ").mkString("/")
        === "우와(Korean: 0, 2)/!!!(Punctuation: 2, 3)/ (Space: 5, 1)/완전(Korean: 6, 2)/" +
        "ㅋㅋㅋㅋ(KoreanParticle: 8, 4)"
    )

    assert(
      chunk("@nlpenguin @edeng #korean_tokenizer_rocks 우하하").mkString("/")
        === "@nlpenguin(ScreenName: 0, 10)/ (Space: 10, 1)/@edeng(ScreenName: 11, 6)/" +
        " (Space: 17, 1)/#korean_tokenizer_rocks(Hashtag: 18, 23)/ (Space: 41, 1)/" +
        "우하하(Korean: 42, 3)"
    )
  }
}