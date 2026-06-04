-- =======================================================================
-- 유저 ID: 6 번 기획 매트릭스 반영 최종 더미 데이터 스크립트 (PART 1)
-- =======================================================================

-- -----------------------------------------------------------------------
-- [1] 초등학교 1~2학년 (ELEM_1_2) - 총 46개 데이터
-- 기획: 1월(5), 2월(3), 3월(4), 4월(10), 5월(7), 6월(5), 8월(8), 11월(3), 12월(1)
-- -----------------------------------------------------------------------

-- 1월 (5개) : 점수대 52~58점
-- =======================================================================
-- 윤지님 최종 DDL 스키마 반영 더미 데이터 스크립트
-- 제거 / completeness_score, word_count 추가)
-- =======================================================================

-- -----------------------------------------------------------------------
-- [1] 초등학교 1~2학년 (ELEM_1_2) - 총 46개 데이터 completeness_score
-- -----------------------------------------------------------------------

-- 1월 (5개)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내 소개하기 1회차', 's3://talktalk/audio/e1_01.wav', 42, 'ELEM_1_2', 'COMPLETED', '2026-01-03 10:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '안녕하세요 저는 일학년 삼반 김윤지입니다.', 54.0, 52.0, 53.0, 48.0, 53.0, 6, 49.0, 51.0, 52.0, '목소리가 참 예뻐요! 다음에는 좋아하는 장난감도 말해볼까요?', '2026-01-03 10:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내 소개하기 2회차', 's3://talktalk/audio/e1_02.wav', 45, 'ELEM_1_2', 'COMPLETED', '2026-01-08 11:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '안녕하세요 저는 일학년이고요 우리 집에는 강아지가 있습니다.', 56.0, 53.0, 54.0, 49.0, 54.0, 8, 52.0, 52.0, 51.0, '강아지 이야기를 꺼낸 점이 좋아요. 이름도 함께 소개해 주세요.', '2026-01-08 11:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내 소개하기 2회차', 's3://talktalk/audio/e1_02.wav', 45, 'ELEM_1_2', 'COMPLETED', '2026-01-10 11:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '안녕하세요 저는 일학년이고요 우리 집에는 강아지가 있습니다.', 56.0, 53.0, 49.0, 54.0, 52.0, 6, 52.0, 51.0, 49.0, '강아지 이야기를 꺼낸 점이 좋아요. 이름도 함께 소개해 주세요.', '2026-01-08 11:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 좋아하는 과일', 's3://talktalk/audio/e1_03.wav', 38, 'ELEM_1_2', 'COMPLETED', '2026-01-14 15:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '저는 딸기를 좋아합니다 딸기는 달콤하고 맛있습니다.', 57.0, 54.0, 51.0, 56.0, 53.0, 6,53.0, 53.0, 52.0, '달콤하다는 표현을 아주 잘 사용했어요. 무슨 색인지도 말해봐요.', '2026-01-14 15:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 엄마 자랑', 's3://talktalk/audio/e1_04.wav', 50, 'ELEM_1_2', 'COMPLETED', '2026-01-21 09:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '우리 엄마는 요리를 잘하십니다 떡볶이를 맛있게 해주십니다.', 59.0, 56.0, 52.0, 57.0, 54.0, 6, 56.0, 54.0, 53.0, '엄마가 해주시는 떡볶이 생각이 전해지네요. 씩씩하게 잘 읽었습니다.', '2026-01-21 09:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '방학 때 가고 싶은 곳', 's3://talktalk/audio/e1_05.wav', 48, 'ELEM_1_2', 'COMPLETED', '2026-01-28 16:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '바다에 가고 싶습니다 바다에서 수영을 하면 시원합니다.', 58.0, 57.0, 53.0, 58.0, 56.0, 6, 54.0, 56.0, 54.0, '시원한 바다 풍경이 그려져요. 다음엔 누구랑 가고 싶은지도 말해봐요.', '2026-01-28 16:10:00');

-- 2월 (3개) : 점수대 58~61점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '가장 친한 친구', 's3://talktalk/audio/e1_06.wav', 52, 'ELEM_1_2', 'COMPLETED', '2026-02-04 11:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '내 친구 민우는 착합니다 저랑 매일 놀이터에서 놉니다.', 61.0, 58.0, 54.0, 59.0, 57.0, 6, 58.0, 57.0, 56.0, '친구와 재미있게 노는 모습이 보기 좋습니다. 주어 호응이 바릅니다.', '2026-02-04 11:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 잘하는 것', 's3://talktalk/audio/e1_07.wav', 46, 'ELEM_1_2', 'COMPLETED', '2026-02-13 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '저는 그림 그리기를 잘합니다 멋진 자동차를 그릴 수 있습니다.', 62.0, 59.0, 56.0, 61.0, 58.0, 6, 59.0, 58.0, 57.0, '멋진 자동차 그림이 기대되는 발표네요. 단어가 적절합니다.', '2026-02-13 14:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '좋아하는 색깔', 's3://talktalk/audio/e1_08.wav', 41, 'ELEM_1_2', 'COMPLETED', '2026-02-22 10:05:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '저는 파란색이 좋습니다 파란 하늘을 보면 기분이 맑아집니다.', 63.0, 61.0, 57.0, 62.0, 59.0, 6, 61.0, 59.0, 58.0, '맑아진다는 고운 표현을 참 잘 써주었어요. 완성도 높은 문장입니다.', '2026-02-22 10:05:00');

-- 3월 (4개) : 점수대 61~64점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '새 학년이 되어서', 's3://talktalk/audio/e1_09.wav', 55, 'ELEM_1_2', 'COMPLETED', '2026-03-03 09:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '새로운 교실에 가니 신기합니다 친구들을 많이 사귀고 싶습니다.', 64.0, 62.0, 58.0, 63.0, 61.0, 6, 62.0, 61.0, 59.0, '새 학기의 설렘이 잘 묻어납니다. 끝맺음 발음이 정확해졌어요.', '2026-03-03 09:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '봄바람 느낌', 's3://talktalk/audio/e1_10.wav', 43, 'ELEM_1_2', 'COMPLETED', '2026-03-11 13:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '봄바람이 부니 따뜻합니다 학교 오는 길이 아주 즐겁습니다.', 66.0, 63.0, 59.0, 64.0, 62.0, 6,64.0, 62.0, 61.0, '따뜻하고 즐거운 마음이 목소리 흐름에서 잘 느껴집니다.', '2026-03-11 13:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내 짝꿍 이야기', 's3://talktalk/audio/e1_11.wav', 49, 'ELEM_1_2', 'COMPLETED', '2026-03-19 11:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '내 짝꿍은 지우개 빌려주었습니다 참 고맙고 다정한 친구입니다.', 65.0, 64.0, 61.0, 66.0, 63.0, 6,63.0, 63.0, 62.0, '고마운 마음을 말로 표현해냈네요. 조사를 조금만 더 정확하게 넣어봐요.', '2026-03-19 11:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '좋아하는 책', 's3://talktalk/audio/e1_12.wav', 58, 'ELEM_1_2', 'COMPLETED', '2026-03-27 16:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '동화책 신데렐라가 재미있습니다 구두를 잃어버리는 장면이 슬픕니다.', 67.0, 66.0, 62.0, 67.0, 64.0, 6,66.0, 64.0, 63.0, '슬펐던 장면을 인상 깊게 잘 묘사했습니다. 시작-중간 구조가 잡혀있어요.', '2026-03-27 16:30:00');

-- 4월 (10개) : 점수대 64~68점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '놀이공원 추억 1', 's3://talktalk/audio/e1_13.wav', 53, 'ELEM_1_2', 'COMPLETED', '2026-04-02 10:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '주말에 회전목마를 탔습니다 뱅글뱅글 돌아서 정말 재미있었습니다.', 66.0, 64.0, 61.0, 66.0, 66.0, 6,64.0, 64.0, 62.0, '뱅글뱅글 같은 흉내 내는 말을 넣어 표현이 훨씬 생생해졌습니다.', '2026-04-02 10:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '봄 소풍 계획', 's3://talktalk/audio/e1_14.wav', 62, 'ELEM_1_2', 'COMPLETED', '2026-04-05 14:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '소풍을 가면 김밥을 먹을 것입니다 김밥에는 햄이 들어있어 맛있습니다.', 68.0, 65.0, 63.0, 68.0, 67.0, 6,66.0, 66.0, 64.0, '맛있는 김밥의 특징을 명확한 인과 표현으로 훌륭히 구사했습니다.', '2026-04-05 14:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 키우는 식물', 's3://talktalk/audio/e1_15.wav', 47, 'ELEM_1_2', 'COMPLETED', '2026-04-08 11:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '방에 방울토마토 화분이 있습니다 물을 주면 무럭무럭 자랍니다.', 69.0, 67.0, 64.0, 69.0, 68.0, 6,67.0, 67.0, 66.0, '무럭무럭 자라는 화분 관찰 일기처럼 전달력이 매우 풍부해요.', '2026-04-08 11:50:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '아빠랑 요리', 's3://talktalk/audio/e1_16.wav', 51, 'ELEM_1_2', 'COMPLETED', '2026-04-11 16:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '일요일에 아빠와 볶음밥을 만들었습니다 내가 계란을 깨트렸습니다.', 67.0, 66.0, 62.0, 67.0, 69.0, 6,68.0, 66.0, 64.0, '본인이 참여한 과정을 순서대로 차분하게 잘 나열하여 전달했습니다.', '2026-04-11 16:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '과학의 날 행사', 's3://talktalk/audio/e1_17.wav', 59, 'ELEM_1_2', 'COMPLETED', '2026-04-14 13:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '물로켓을 날렸는데 하늘 높이 올라갔습니다 비행기처럼 빨랐습니다.', 71.0, 68.0, 64.0, 71.0, 71.0, 6,69.0, 68.0, 67.0, '비유적 표현인 비행기를 사용하여 속도감을 직관적으로 잘 묘사했습니다.', '2026-04-14 13:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '좋아하는 인형', 's3://talktalk/audio/e1_18.wav', 44, 'ELEM_1_2', 'COMPLETED', '2026-04-17 10:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '침대에 폭신한 곰인형이 있습니다 밤에 안고 자면 무섭지 않습니다.', 69.0, 69.0, 66.0, 72.0, 72.0, 6,71.0, 69.0, 68.0, '폭신하다는 감각적 어휘를 통해 곰인형의 안락함을 생생히 안겨줍니다.', '2026-04-17 10:45:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우산 쓰는 날', 's3://talktalk/audio/e1_19.wav', 48, 'ELEM_1_2', 'COMPLETED', '2026-04-21 15:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '노란색 우산을 쓰면 기분이 좋습니다 빗방울 소리가 톡톡 들립니다.', 72.0, 71.0, 67.0, 73.0, 73.0, 6,72.0, 71.0, 69.0, '의성어 톡톡의 배치가 훌륭해요. 문장의 리듬감이 잘 살아있습니다.', '2026-04-21 15:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '태권도장 연습', 's3://talktalk/audio/e1_20.wav', 56, 'ELEM_1_2', 'COMPLETED', '2026-04-24 17:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '오늘 발차기 연습을 열심히 했습니다 관장님이 멋지다고 칭찬했습니다.', 71.0, 72.0, 68.0, 74.0, 74.0, 6,73.0, 72.0, 71.0, '연습 성취 과정을 원인과 결과의 완전한 형태로 씩씩하게 구사했습니다.', '2026-04-24 17:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '할머니 댁 방문', 's3://talktalk/audio/e1_21.wav', 63, 'ELEM_1_2', 'COMPLETED', '2026-04-26 11:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '시골 할머니 댁 마당에는 꽃이 피었습니다 나비가 날아다녔습니다.', 73.0, 73.0, 69.0, 74.0, 73.0, 6, 74.0, 73.0, 72.0, '마당 풍경 속 꽃과 나비를 조화롭고 완결성 높은 문장들로 풀어냈습니다.', '2026-04-26 11:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '학교 급식 시간', 's3://talktalk/audio/e1_22.wav', 50, 'ELEM_1_2', 'COMPLETED', '2026-04-29 13:05:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '오늘 급식에 돈가스가 나왔습니다 너무 맛있어서 다 먹었습니다.', 74.0, 74.0, 71.0, 76.0, 76.0, 6, 74.0, 74.0, 73.0, '원인과 결과 문맥 호응이 완벽합니다. 발화 유창성 또한 크게 개선되었습니다.', '2026-04-29 13:05:00');

-- 5월 (7개) : 점수대 68~72점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '어린이날 선물', 's3://talktalk/audio/e1_23.wav', 55, 'ELEM_1_2', 'COMPLETED', '2026-05-05 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '어린이날에 블록 장난감을 받았습니다 성을 만들면 아주 멋집니다.', 73.0, 72.0, 69.0, 74.0, 76.0, 6, 73.0, 72.0, 71.0, '성을 만드는 구체적인 행동을 문맥에 알맞게 바른 소리로 잘 표현했어요.', '2026-05-05 10:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '어버이날 카네이션', 's3://talktalk/audio/e1_24.wav', 61, 'ELEM_1_2', 'COMPLETED', '2026-05-08 14:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '종이 카네이션을 직접 접었습니다 부모님께 드리니 웃으셨습니다.', 74.0, 73.0, 71.0, 76.0, 77.0, 6, 74.0, 74.0, 72.0, '직접 정성을 드린 흐름이 인과 문장 구조 속에서 자연스레 빛납니다.', '2026-05-08 14:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '스승의 날 감사', 's3://talktalk/audio/e1_25.wav', 48, 'ELEM_1_2', 'COMPLETED', '2026-05-15 09:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '선생님께 고맙습니다 노래를 가르쳐주실 때 참 즐겁습니다.', 76.0, 74.0, 72.0, 77.0, 78.0, 6, 76.0, 76.0, 74.0, '선생님께 감사함을 담은 완전한 복문을 안정적인 끊어 읽기로 소화했어요.', '2026-05-15 09:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '주말 자전거 타기', 's3://talktalk/audio/e1_26.wav', 57, 'ELEM_1_2', 'COMPLETED', '2026-05-19 16:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '공원에서 아빠와 네발자전거를 탔습니다 바람이 불어 엄청 시원했습니다.', 77.0, 76.0, 73.0, 78.0, 79.0, 6, 77.0, 77.0, 76.0, '공간적 맥락과 감각 표현의 연결이 매끄러워 한눈에 다가옵니다.', '2026-05-19 16:45:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '장미꽃 축제', 's3://talktalk/audio/e1_27.wav', 50, 'ELEM_1_2', 'COMPLETED', '2026-05-22 11:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '빨간색 장미꽃이 활짝 피었습니다 마당 향기가 참 좋습니다.', 76.0, 77.0, 74.0, 79.0, 78.0, 6, 78.0, 76.0, 76.0, '활짝이라는 어휘 선택이 훌륭하며 문장 리듬 구성의 연결이 완벽합니다.', '2026-05-22 11:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 가꾼 화단', 's3://talktalk/audio/e1_28.wav', 53, 'ELEM_1_2', 'COMPLETED', '2026-05-26 13:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '작은 새싹이 드디어 올라왔습니다 아침마다 관찰하니 신기합니다.', 78.0, 78.0, 76.0, 81.0, 81.0, 6, 79.0, 78.0, 77.0, '드디어, 아침마다 같은 연결어 장치가 훌륭한 문맥을 완성해 냈습니다.', '2026-05-26 13:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름 옷 준비', 's3://talktalk/audio/e1_29.wav', 44, 'ELEM_1_2', 'COMPLETED', '2026-05-30 15:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '반팔 셔츠를 서랍에서 꺼냈습니다 날씨가 더워졌기 때문입니다.', 79.0, 79.0, 77.0, 82.0, 83.0, 6, 81.0, 79.0, 78.0, '인과 관계 연결 어미(~때문입니다)를 저학년 발달 수준에 맞춰 적절히 구사했습니다.', '2026-05-30 15:10:00');

-- 6월 (5개) : 점수대 72~74점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름철 곤충 관찰', 's3://talktalk/audio/e1_30.wav', 58, 'ELEM_1_2', 'COMPLETED', '2026-06-02 10:25:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '놀이터 나무에서 매미가 맴맴 웁니다 여름이 찾아온 것 같습니다.', 79.0, 78.0, 76.0, 82.0, 81.0, 6, 82.0, 81.0, 79.0, '맴맴 같은 소리 지표와 인과 추론 문맥을 자연스럽게 묶어 전달했어요.', '2026-06-02 10:25:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '시원한 수박 식사', 's3://talktalk/audio/e1_31.wav', 49, 'ELEM_1_2', 'COMPLETED', '2026-06-09 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '커다란 수박을 칼로 슥슥 잘랐습니다 빨간 속이 엄청 달콤합니다.', 81.0, 79.0, 77.0, 83.0, 83.0, 6, 81.0, 82.0, 81.0, '슥슥, 커다란 같은 풍부한 형용 표현 덕분에 스피치가 생생합니다.', '2026-06-09 14:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '모래성 쌓기 놀이', 's3://talktalk/audio/e1_32.wav', 62, 'ELEM_1_2', 'COMPLETED', '2026-06-16 11:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '놀이터 모래밭에서 두꺼비집을 지었습니다 손이 까매져도 즐거웠습니다.', 82.0, 81.0, 78.0, 84.0, 84.0, 6, 83.0, 83.0, 82.0, '조건 양보 어미(~해도)의 활용이 매우 신선하고 문장 리듬이 탄탄합니다.', '2026-06-16 11:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '시원한 아이스크림', 's3://talktalk/audio/e1_33.wav', 41, 'ELEM_1_2', 'COMPLETED', '2026-06-22 16:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '초코 아이스크림을 한 입 먹었습니다 입안이 꽁꽁 얼어붙었습니다.', 81.0, 82.0, 79.0, 86.0, 86.0, 6, 84.0, 83.0, 82.0, '꽁꽁 얼어붙었다는 감각의 전이가 문장 속에서 매우 우수하게 표현됨.', '2026-06-22 16:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름 밤하늘 관찰', 's3://talktalk/audio/e1_34.wav', 55, 'ELEM_1_2', 'COMPLETED', '2026-06-29 09:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '여름 밤하늘에 별이 반짝반짝 빛납니다 시골 밤하늘은 참 맑습니다.', 83.0, 83.0, 81.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '반짝반짝 시각 어휘를 활용한 시작-중간-끝 완결성이 눈에 띕니다.', '2026-06-29 09:50:00');

-- 7월 (0개) - 공백 조건 통과

-- 8월 (8개) : 점수대 74~77점 (여름방학 성장기)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '바닷가 조개 줍기', 's3://talktalk/audio/e1_35.wav', 60, 'ELEM_1_2', 'COMPLETED', '2026-08-02 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '백사장에서 하얀 조개껍데기를 주웠습니다 동생에게 선물로 주었습니다.', 83.0, 81.0, 78.0, 84.0, 84.0, 6, 83.0, 82.0, 81.0, '동작이 유기적인 선후 관계로 정렬되어 말하기 완결성이 도드라집니다.', '2026-08-02 11:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '물놀이터 물총싸움', 's3://talktalk/audio/e1_36.wav', 52, 'ELEM_1_2', 'COMPLETED', '2026-08-06 14:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '친구들과 물총싸움을 신나게 했습니다 온몸이 다 젖어도 하하 웃었습니다.', 84.0, 82.0, 79.0, 86.0, 86.0, 6, 84.0, 84.0, 83.0, '하하 같은 웃음 지표와 문법적 성분 간 호응이 자연스럽습니다.', '2026-08-06 14:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '캠핑장 밤하늘', 's3://talktalk/audio/e1_37.wav', 68, 'ELEM_1_2', 'COMPLETED', '2026-08-11 20:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '산속 캠핑장에서 밤하늘을 보았습니다 별똥별이 슥 떨어져서 소원을 빌었습니다.', 84.0, 84.0, 81.0, 87.0, 87.0, 6, 86.0, 84.0, 84.0, '별똥별 발견 후 소원을 비는 서사 순서가 매우 꼼꼼히 구성됨.', '2026-08-11 20:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '계곡 물고기 잡기', 's3://talktalk/audio/e1_38.wav', 57, 'ELEM_1_2', 'COMPLETED', '2026-08-14 13:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '투명한 계곡물속에 작은 물고기가 다 보였습니다 발을 담그니 얼음처럼 차가웠습니다.', 86.0, 84.0, 82.0, 88.0, 89.0, 6, 87.0, 86.0, 84.0, '얼음처럼 이라는 직유적 묘사가 스피치의 격을 높였습니다.', '2026-08-14 13:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '할머니네 참외 맛', 's3://talktalk/audio/e1_39.wav', 49, 'ELEM_1_2', 'COMPLETED', '2026-08-18 16:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '할머니가 깎아주신 노란 참외는 속이 달콤했습니다 아삭아삭 씹는 소리가 났습니다.', 86.0, 86.0, 83.0, 89.0, 88.0, 6, 88.0, 87.0, 86.0, '아삭아삭 청각적 심상을 낱말 선택 속에 훌륭하게 배정해 냈습니다.', '2026-08-18 16:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '방학 숙제 달성', 's3://talktalk/audio/e1_40.wav', 71, 'ELEM_1_2', 'COMPLETED', '2026-08-22 10:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '매일 그렸던 그림일기장을 드디어 다 채웠습니다 부모님이 대단하다고 하셨습니다.', 87.0, 86.0, 84.0, 91.0, 92.0, 6, 89.0, 88.0, 87.0, '드디어 라는 도구어를 바탕으로 성취 목적 의식을 조리 있게 기술함.', '2026-08-22 10:45:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '바다 미술 그리기', 's3://talktalk/audio/e1_41.wav', 53, 'ELEM_1_2', 'COMPLETED', '2026-08-25 15:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '도화지에 파란 크레파스로 바다를 크게 칠했습니다 고래도 한 마리 그려 넣었습니다.', 88.0, 87.0, 86.0, 92.0, 91.0, 6, 91.0, 89.0, 88.0, '공간 배열과 요소 채우기가 인덱싱 형태로 완벽한 리듬을 보여줌.', '2026-08-25 15:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '방학 끝 개학 준비', 's3://talktalk/audio/e1_42.wav', 64, 'ELEM_1_2', 'COMPLETED', '2026-08-29 09:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '내일 개학이라 책가방에 필통을 챙겼습니다 친구들을 볼 생각에 신이 납니다.', 89.0, 88.0, 87.0, 93.0, 93.0, 6, 92.0, 91.0, 89.0, '원인 상황 판단과 감정 추론이 저학년 최고 수준의 정합성을 가짐.', '2026-08-29 09:20:00');

-- 9월, 10월 (0개) - 공백 조건 통과

-- 11월 (3개) : 점수대 77~79점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '단풍잎 가을 편지', 's3://talktalk/audio/e1_43.wav', 50, 'ELEM_1_2', 'COMPLETED', '2026-11-06 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '길에서 주운 빨간 단풍잎을 편지지에 붙였습니다 가을 냄새가 물씬 풍깁니다.', 89.0, 88.0, 86.0, 93.0, 92.0, 6, 92.0, 91.0, 89.0, '물씬 풍긴다는 감각 부사 활용 및 호흡 구조 배치가 유려합니다.', '2026-11-06 14:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '낙엽 밟는 소리', 's3://talktalk/audio/e1_44.wav', 47, 'ELEM_1_2', 'COMPLETED', '2026-11-15 11:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '바스락 낙엽을 밟으며 운동장을 걸었습니다 소리가 과자처럼 바삭했습니다.', 91.0, 89.0, 87.0, 94.0, 93.0, 6,93.0, 92.0, 91.0, '낙엽 소리를 과자에 비유하여 신선하고 창의적인 낱말 결합을 보여줌.', '2026-11-15 11:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '따뜻한 붕어빵', 's3://talktalk/audio/e1_45.wav', 54, 'ELEM_1_2', 'COMPLETED', '2026-11-24 16:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '추운 날 엄마가 사 오신 붕어빵을 먹었습니다 꼬리 부분에 팥이 가득했습니다.', 91.0, 91.0, 89.0, 94.0, 94.0, 6, 94.0, 93.0, 92.0, '세부 묘사력이 극대화되어 청중 흡입력이 우수한 고품질 스피치임.', '2026-11-24 16:30:00');

-- 12월 (1개) : 최종 정점 80점 돌파 (ELEM_1_2 최종 성장작)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '2학년이 되면 하고 싶은 일', 's3://talktalk/audio/e1_46.wav', 85, 'ELEM_1_2', 'COMPLETED', '2026-12-15 11:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '이학년이 되면 줄넘기를 백 번 넘게 할 것입니다. 왜냐하면 더 튼튼해지고 싶기 때문입니다. 그리고 친구들도 많이 도울 것입니다.', 93.0, 92.0, 88.0, 96.0, 97.0, 6,94.0, 93.0, 92.0, '문장 성분의 완벽한 조화와 명확한 원인-결과 구조가 기대를 압도적으로 초과함.', '2026-12-15 11:30:00');


-- -----------------------------------------------------------------------
-- [2] 초등학교 3~4학년 (ELEM_3_4) - 총 24개 데이터
-- 기획: 1~2월(0), 3월(10), 4월(3), 5월(2), 6월(5), 7월(4), 8~12월(0)
-- -----------------------------------------------------------------------

-- 1월, 2월 (0개) - 공백 조건 통과

-- 3월 (10개) : 점수대 61~64점 (새로운 학년 시작)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '3학년 선언 다짐', 's3://talktalk/audio/e3_01.wav', 68, 'ELEM_3_4', 'COMPLETED', '2026-03-02 09:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '이제 삼학년이니까 스스로 숙제를 제때 하겠습니다. 일기도 밀리지 않고 쓰겠습니다.', 63.0, 61.0, 56.0, 62.0, 59.0, 6, 62.0, 61.0, 58.0, '다짐이 명확하나 단문 나열에 가깝습니다. 인과 연결어를 의식해 보아요.', '2026-03-02 09:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '새 교과서를 받고', 's3://talktalk/audio/e3_02.wav', 72, 'ELEM_3_4', 'COMPLETED', '2026-03-05 14:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '교과서에서 새 책 냄새가 나서 기분이 좋았습니다 수학이 재미있어 보입니다.', 64.0, 61.0, 57.0, 63.0, 61.0, 6, 61.0, 59.0, 59.0, '새 책을 받은 기쁨이 살아있어요. 문장 호응이 다소 흔들리니 주의해요.', '2026-03-05 14:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '교실 자리 바꾸기', 's3://talktalk/audio/e3_03.wav', 59, 'ELEM_3_4', 'COMPLETED', '2026-03-09 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '제비뽑기로 창가 자리가 되었습니다 햇빛이 잘 들어서 명당자리입니다.', 66.0, 63.0, 58.0, 64.0, 62.0, 6, 63.0, 62.0, 61.0, '명당자리라는 어휘 사용이 흥미롭네요. 이유 제시가 명확합니다.', '2026-03-09 11:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '급식실 규칙 안내', 's3://talktalk/audio/e3_04.wav', 81, 'ELEM_3_4', 'COMPLETED', '2026-03-12 13:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '급식실에서는 한 줄로 걸어가야 급판을 쏟지 않습니다 뛰면 위험합니다.', 64.0, 64.0, 59.0, 66.0, 63.0, 6, 62.0, 63.0, 62.0, '급판이라는 발음 실수가 있었지만 조건문 구사 방식 시도가 좋습니다.', '2026-03-12 13:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 뽑은 반장', 's3://talktalk/audio/e3_05.wav', 74, 'ELEM_3_4', 'COMPLETED', '2026-03-15 16:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '오늘 반장 선거를 했는데 내 친구 민호가 뽑혔습니다 공약을 잘 말했습니다.', 65.0, 64.0, 61.0, 66.0, 64.0, 6, 64.0, 64.0, 63.0, '선거 상황 전달이 침착해요. 주어-서술어 연결 무난합니다.', '2026-03-15 16:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '새로운 방과후 교실', 's3://talktalk/audio/e3_06.wav', 60, 'ELEM_3_4', 'COMPLETED', '2026-03-18 15:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '방과후 로봇과학 수업을 신청했습니다 직접 조립하니까 로봇이 움직였습니다.', 67.0, 66.0, 62.0, 67.0, 66.0, 6, 66.0, 64.0, 63.0, '선후 인과 원인이 명시되어 내용 조직이 한결 체계적으로 잡힘.', '2026-03-18 15:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '봄비 오는 등굣길', 's3://talktalk/audio/e3_07.wav', 63, 'ELEM_3_4', 'COMPLETED', '2026-03-22 08:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '봄비가 내려서 땅이 촉촉해졌습니다 장화를 신어서 물웅덩이도 첨벙 들어갔습니다.', 68.0, 67.0, 63.0, 68.0, 67.0, 6, 67.0, 66.0, 64.0, '첨벙, 촉촉 같은 어휘 배치가 돋보이며 전달 문법력이 우수합니다.', '2026-03-22 08:50:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리반 체육시간', 's3://talktalk/audio/e3_08.wav', 77, 'ELEM_3_4', 'COMPLETED', '2026-03-25 11:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '체육시간 피구를 했는데 우리 팀이 이겼습니다 내가 마지막 공을 잡았습니다.', 69.0, 68.0, 64.0, 69.0, 68.0, 6, 68.0, 67.0, 66.0, '이야기 중심 골격(처음-가운데) 구성이 매끄럽게 정리되었습니다.', '2026-03-25 11:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '수학 오답노트 정리', 's3://talktalk/audio/e3_09.wav', 85, 'ELEM_3_4', 'COMPLETED', '2026-03-29 16:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '틀린 문제를 오답노트에 다시 풀었습니다 왜 틀렸는지 알게 되어서 기쁩니다.', 69.0, 69.0, 66.0, 71.0, 71.0, 6, 69.0, 68.0, 67.0, '문장 성분의 호응 관계가 3학년 발달 평균 레벨에 적절히 부합함.', '2026-03-29 16:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '주말 동네 도서관', 's3://talktalk/audio/e3_10.wav', 92, 'ELEM_3_4', 'COMPLETED', '2026-03-31 14:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '도서관 어린이실에서 과학 만화책을 보았습니다 내일 또 가고 싶을 만큼 재밌습니다.', 71.0, 71.0, 67.0, 73.0, 73.0, 6, 72.0, 71.0, 69.0, '정도가 담긴 복문 표현(~할 만큼) 구사를 명확하게 달성했습니다.', '2026-03-31 14:30:00');

-- 4월 (3개) : 점수대 64~67점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '식목일 나무 심기 행사', 's3://talktalk/audio/e3_11.wav', 88, 'ELEM_3_4', 'COMPLETED', '2026-04-05 10:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '식목일에 화분에 상추 씨앗을 심었습니다 상추가 자라 고기에 싸 먹을 것입니다.', 72.0, 71.0, 68.0, 74.0, 74.0, 6, 73.0, 72.0, 71.0, '미래 행동 계획을 인과관계 순서대로 조리 있게 기술해 주었습니다.', '2026-04-05 10:45:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '과학의 날 그리기 대외', 's3://talktalk/audio/e3_12.wav', 95, 'ELEM_3_4', 'COMPLETED', '2026-04-16 15:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '미래의 우주 도시를 상상해서 그렸습니다 하늘을 나는 자동차가 많아서 신기했습니다.', 73.0, 73.0, 69.0, 74.0, 73.0, 6, 74.0, 73.0, 72.0, '공간 상황 기술의 일관성이 양호하며 발음 유창성이 우수합니다.', '2026-04-16 15:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '봄맞이 대청소', 's3://talktalk/audio/e3_13.wav', 74, 'ELEM_3_4', 'COMPLETED', '2026-04-24 13:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '내 방 책상을 깨끗이 정리했습니다 먼지가 사라지니 공부가 더 잘되는 느낌입니다.', 74.0, 74.0, 71.0, 76.0, 76.0, 6, 74.0, 74.0, 73.0, '원인 상황 분석과 결과 추론 구조의 조화가 훌륭한 문장을 이룸.', '2026-04-24 13:20:00');

-- 5월 (2개) : 점수대 67~70점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 할머니 생신 축하', 's3://talktalk/audio/e3_14.wav', 82, 'ELEM_3_4', 'COMPLETED', '2026-05-09 11:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '할머니 생신 잔치에 가서 축하 노래를 불렀습니다 할머니가 기뻐서 웃으셨습니다.', 74.0, 74.0, 72.0, 77.0, 78.0, 6, 76.0, 76.0, 74.0, '경어법 표현 대상 호응(할머니~웃으셨습니다)을 정확히 지켰습니다.', '2026-05-09 11:00:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '가족 나들이 동물원', 's3://talktalk/audio/e3_15.wav', 101, 'ELEM_3_4', 'COMPLETED', '2026-05-23 16:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '동물원에서 거대한 코끼리를 보았습니다 코로 풀을 집어 먹는 모습이 기억에 남습니다.', 77.0, 76.0, 73.0, 78.0, 79.0, 6, 77.0, 77.0, 76.0, '거대한 관찰 관형 표현과 세부 집중 묘사가 아주 생생하게 연결됨.', '2026-05-23 16:15:00');

-- 6월 (5개) : 점수대 70~73점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름철 위생 안전 발표', 's3://talktalk/audio/e3_16.wav', 90, 'ELEM_3_4', 'COMPLETED', '2026-06-04 10:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '외출하고 돌아오면 비누로 손을 씻어야 배가 아프지 않습니다 세균이 사라지기 때문입니다.', 76.0, 77.0, 74.0, 79.0, 78.0, 6, 78.0, 76.0, 76.0, '인과 구조 배치(~때문입니다)를 34학년 기준에 맞게 완결성 있게 채움.', '2026-06-04 10:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 동네 시장 구경', 's3://talktalk/audio/e3_17.wav', 83, 'ELEM_3_4', 'COMPLETED', '2026-06-11 15:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '재래시장에 가서 맛있는 떡볶이를 먹었습니다 마트보다 활기찬 느낌이라 신기했습니다.', 78.0, 78.0, 76.0, 81.0, 81.0, 6, 79.0, 78.0, 77.0, '비교 표현(마트보다)과 활기찬 개념 어휘 결합 능력이 눈에 띕니다.', '2026-06-11 15:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름 장마철 조심할 일', 's3://talktalk/audio/e3_18.wav', 94, 'ELEM_3_4', 'COMPLETED', '2026-06-18 11:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '비가 많이 올 때는 맨홀 뚜껑 위를 밟으면 안 됩니다 미끄러져서 다칠 위험이 있습니다.', 79.0, 79.0, 77.0, 82.0, 83.0, 6, 81.0, 79.0, 78.0, '안전 수칙의 원인 분석 행동 제시 흐름이 완벽한 정합성을 나타냄.', '2026-06-18 11:45:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '시원한 물하천 산책', 's3://talktalk/audio/e3_19.wav', 79, 'ELEM_3_4', 'COMPLETED', '2026-06-23 16:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '저녁에 가족들과 천변을 걸었습니다 분수가 높이 뿜어져 나와서 엄청 청량했습니다.', 79.0, 78.0, 76.0, 82.0, 81.0, 6,82.0, 81.0, 79.0, '청량했다는 높은 수준의 개념 수식어 사용 성취도가 돋보입니다.', '2026-06-23 16:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리의 지구 사랑 방법', 's3://talktalk/audio/e3_20.wav', 105, 'ELEM_3_4', 'COMPLETED', '2026-06-27 09:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '양치를 할 때 컵에 물을 받아 쓰면 물을 아낄 수 있습니다 작은 실천이 중요합니다.', 81.0, 79.0, 77.0, 83.0, 83.0, 6, 81.0, 82.0, 81.0, '주제 의견과 원인 근거 구조 선후 배치가 단단하게 고정되었습니다.', '2026-06-27 09:30:00');

-- 7월 (4개) : 점수대 73~76점 (ELEM_3_4 정점 최고 기록 구간)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 시골집 과수원 발표', 's3://talktalk/audio/e3_21.wav', 112, 'ELEM_3_4', 'COMPLETED', '2026-07-04 14:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '외갓집 자두나무에 열매가 주렁주렁 열렸습니다 하나 먹으니 입안에 새콤한 즙이 가득했습니다.', 82.0, 81.0, 78.0, 84.0, 84.0, 6, 83.0, 83.0, 82.0, '주렁주렁, 새콤한 표현 장치가 어우러진 처음-가운데 완결 구조가 참 좋습니다.', '2026-07-04 14:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름철 식중독 예방 규칙', 's3://talktalk/audio/e3_22.wav', 96, 'ELEM_3_4', 'COMPLETED', '2026-07-12 11:05:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '날음식을 먹으면 여름에는 쉽게 배가 탈이 날 수 있습니다. 그러므로 반드시 끓여서 익혀 먹어야 안전합니다.', 81.0, 82.0, 79.0, 86.0, 86.0, 6, 84.0, 83.0, 82.0, '그러므로 인과 부사 접속어를 논거 흐름에 맞춰 알맞게 잘 다루었습니다.', '2026-07-12 11:05:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '박물관 유물 견학 리포트', 's3://talktalk/audio/e3_23.wav', 120, 'ELEM_3_4', 'COMPLETED', '2026-07-19 15:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '역사박물관에서 옛날 백제 왕관을 직접 감상했습니다 황금빛 무늬가 대단히 정교해서 감탄이 나왔습니다.', 83.0, 83.0, 81.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '정교해서 라는 고등 어휘 적용 및 발음 끊어 읽기 유창성이 빼어납니다.', '2026-07-19 15:50:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '나의 진정한 소원 발표', 's3://talktalk/audio/e3_24.wav', 114, 'ELEM_3_4', 'COMPLETED', '2026-07-28 13:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '내 진짜 소원은 유기견들이 좋은 가족을 만나는 것입니다 왜냐하면 생명은 모두 소중하기 때문입니다.', 84.0, 84.0, 82.0, 88.0, 89.0, 6, 87.0, 86.0, 84.0, '주제 타당성과 인지 가치가 완벽히 결합된 34학년 최종 성장 최고 발화 작품입니다.', '2026-07-28 13:00:00');

-- 8월 ~ 12월 (0개) - 공백 조건 통과


-- =======================================================================
-- 초등학교 5~6학년 (ELEM_5_6) 총 48개 전체 더미 데이터
-- 유저 ID: 6 / 기획 매트릭스에 따른 월별 정확한 분포 및 우상향 점수 스케일링
-- =======================================================================

-- -----------------------------------------------------------------------
-- 1월 (2개) : 평균 점수대 58~61점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 존경하는 인물 발표', 's3://talktalk/audio/e5_01.wav', 92, 'ELEM_5_6', 'COMPLETED', '2026-01-06 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '저는 세종대왕을 존경합니다. 백성들을 위해서 한글을 만드셨기 때문입니다.', 61.0, 59.0, 54.0, 62.0, 58.0, 6, 59.0, 57.0, 56.0, '존경하는 인물과 이유가 잘 나타났으나 고학년인 만큼 설명 방법(예시 등)을 더 늘려보세요.', '2026-01-06 11:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '나의 겨울방학 생활', 's3://talktalk/audio/e5_02.wav', 104, 'ELEM_5_6', 'COMPLETED', '2026-01-22 15:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '방학 동안 매일 아침 독서를 했습니다. 책을 읽으니 지식이 풍부해지는 기분이 들었습니다.', 62.0, 61.0, 56.0, 61.0, 59.0, 6, 61.0, 58.0, 59.0, '규칙적인 생활 태도가 돋보이는 발표입니다. 문장 성분의 호응이 무난합니다.', '2026-01-22 15:30:00');

-- -----------------------------------------------------------------------
-- 2월 (3개) : 평균 점수대 61~64점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 가족의 명절 풍경', 's3://talktalk/audio/e5_03.wav', 98, 'ELEM_5_6', 'COMPLETED', '2026-02-05 10:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '설날에 할머니 댁에 모여 떡국을 먹고 세배를 했습니다. 오랜만에 사촌들을 만나서 즐거웠습니다.', 63.0, 62.0, 57.0, 63.0, 61.0, 6, 62.0, 59.0, 61.0, '명절의 유기적인 행동 흐름을 순서대로 잘 나열하여 전달했습니다.', '2026-02-05 10:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '정직한 태도의 중요성', 's3://talktalk/audio/e5_04.wav', 110, 'ELEM_5_6', 'COMPLETED', '2026-02-14 13:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '작은 거짓말이 큰 오해를 부를 수 있습니다. 따라서 우리는 언제나 정직하게 말해야 합니다.', 64.0, 63.0, 59.0, 64.0, 62.0, 6, 64.0, 61.0, 62.0, '주제 의식과 당위 표현이 뚜렷합니다. 문장의 연결이 매끄럽습니다.', '2026-02-14 13:40:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 가보고 싶은 나라', 's3://talktalk/audio/e5_05.wav', 88, 'ELEM_5_6', 'COMPLETED', '2026-02-24 16:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '프랑스 파리에 가고 싶습니다. 에펠탑을 직접 보고 문화재를 감상하고 싶기 때문입니다.', 64.0, 64.0, 58.0, 66.0, 63.0, 6, 63.0, 62.0, 61.0, '가고 싶은 이유를 명확하게 제시했습니다. 개념어 활용을 늘려보세요.', '2026-02-24 16:15:00');

-- -----------------------------------------------------------------------
-- 3월 (4개) : 평균 점수대 64~66점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '5학년을 시작하는 다짐', 's3://talktalk/audio/e5_06.wav', 115, 'ELEM_5_6', 'COMPLETED', '2026-03-03 09:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '고학년이 되었으니 학습 계획을 체계적으로 세우겠습니다. 스스로 부족한 과목을 보충하겠습니다.', 66.0, 63.0, 59.0, 64.0, 64.0, 6, 65.0, 63.0, 62.0, '체계적이라는 개념 어휘 선택이 고학년답고 좋습니다. 실천 방안이 명확합니다.', '2026-03-03 09:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 반 환경 미화', 's3://talktalk/audio/e5_07.wav', 90, 'ELEM_5_6', 'COMPLETED', '2026-03-12 14:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '교실 뒤편에 환경판을 꾸몄습니다. 모두 힘을 합쳐 정리하니 교실이 한결 깔끔해졌습니다.', 66.0, 64.0, 61.0, 66.0, 63.0, 6, 64.0, 64.0, 63.0, '협동 과정을 사실적으로 기술했습니다. 끊어 읽기가 점차 자연스러워집니다.', '2026-03-12 14:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '동아리 활동 선택', 's3://talktalk/audio/e5_08.wav', 101, 'ELEM_5_6', 'COMPLETED', '2026-03-19 11:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '올해는 방송반 동아리를 신청했습니다. 미디어 장비를 직접 다루어보고 싶기 때문입니다.', 67.0, 66.0, 62.0, 67.0, 66.0, 6, 66.0, 64.0, 63.0, '동아리 선택 동기가 인과 관계에 맞춰 체계적으로 조직되었습니다.', '2026-03-19 11:45:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 읽은 과학 잡지', 's3://talktalk/audio/e5_09.wav', 122, 'ELEM_5_6', 'COMPLETED', '2026-03-26 16:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '우주 탐사선에 대한 기사를 읽었습니다. 인류가 화성에 갈 날이 머지않았다는 생각이 들었습니다.', 68.0, 67.0, 63.0, 68.0, 67.0, 6, 67.0, 66.0, 64.0, '정보에 대한 주관적 판단 및 복문 표현 구조를 훌륭히 매칭했습니다.', '2026-03-26 16:20:00');

-- -----------------------------------------------------------------------
-- 4월 (10개) : 평균 점수대 66~69점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '식목일과 산림 보호', 's3://talktalk/audio/e5_10.wav', 110, 'ELEM_5_6', 'COMPLETED', '2026-04-03 10:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '나무를 심는 것은 탄소 흡수에 큰 도움을 줍니다. 무분별한 벌목을 막아야 지구를 지킵니다.', 69.0, 68.0, 64.0, 69.0, 68.0, 6, 68.0, 67.0, 66.0, '벌목, 탄소 흡수 등 고학년에 걸맞은 정확한 어휘를 적재적소에 썼습니다.', '2026-04-03 10:00:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '스마트폰 사용 시간 줄이기', 's3://talktalk/audio/e5_11.wav', 130, 'ELEM_5_6', 'COMPLETED', '2026-04-06 14:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '스마트폰을 너무 오래 보면 시력이 나빠지고 집중력이 감소합니다. 규칙을 정해 사용해야 합니다.', 71.0, 69.0, 66.0, 72.0, 71.0, 6, 69.0, 68.0, 67.0, '감소, 시력 저하 등 구체적인 문제점의 인과 구조 배치가 양호합니다.', '2026-04-06 14:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '과학의 날 발명 아이디어', 's3://talktalk/audio/e5_12.wav', 125, 'ELEM_5_6', 'COMPLETED', '2026-04-09 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '자동으로 분리수거를 해주는 쓰레기통을 상상했습니다. 센서가 물체를 인식해 분류하는 방식입니다.', 72.0, 71.0, 67.0, 73.0, 73.0, 6, 72.0, 71.0, 69.0, '인식, 분류 등 기술 문맥 어휘를 사용하여 아이디어를 논리적으로 잘 풀었습니다.', '2026-04-09 11:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 동네 도서관 활용법', 's3://talktalk/audio/e5_13.wav', 118, 'ELEM_5_6', 'COMPLETED', '2026-04-12 16:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '도서관은 책만 빌리는 곳이 아니라 문화 프로그램도 참여할 수 있는 복합 공간입니다.', 73.0, 71.0, 68.0, 74.0, 72.0, 6, 73.0, 72.0, 71.0, '복합 공간이라는 주위 환경에 대한 정의 표현이 아주 뛰어납니다.', '2026-04-12 16:40:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '올바른 언어 습관의 필요성', 's3://talktalk/audio/e5_14.wav', 105, 'ELEM_5_6', 'COMPLETED', '2026-04-15 13:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '무분별한 줄임말이나 은어는 세대 간의 소통을 단절시킵니다. 바르고 고운 말을 써야 합니다.', 71.0, 72.0, 68.0, 74.0, 74.0, 6, 73.0, 72.0, 71.0, '세대 간 소통 단절이라는 한계의 인과관계를 구조적으로 명확히 짚어냄.', '2026-04-15 13:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '체육 대회 축구 시합', 's3://talktalk/audio/e5_15.wav', 134, 'ELEM_5_6', 'COMPLETED', '2026-04-18 15:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '패스 웍이 중요하다고 생각했습니다. 서로 양보하고 협동했기 때문에 역전승을 거두었습니다.', 73.0, 73.0, 69.0, 74.0, 73.0, 6, 74.0, 73.0, 72.0, '역전승, 패스 웍 같은 경기 맥락 용어를 적절하게 사용하여 묘사함.', '2026-04-18 15:50:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '길고양이 보호에 대한 생각', 's3://talktalk/audio/e5_16.wav', 121, 'ELEM_5_6', 'COMPLETED', '2026-04-21 11:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '길고양이 급식소 운영은 주민 간 갈등을 유발할 수 있으나, 위생적 관리를 통해 공존할 수 있습니다.', 74.0, 74.0, 71.0, 76.0, 76.0, 6, 74.0, 74.0, 73.0, '주민 갈등, 공존 등 다면적인 논점을 고려하여 문장 조화를 이뤄냄.', '2026-04-21 11:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '독서 골든벨 준비 과정', 's3://talktalk/audio/e5_17.wav', 142, 'ELEM_5_6', 'COMPLETED', '2026-04-24 16:05:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '지정 도서를 반복해서 읽으며 핵심 내용을 요약했습니다. 노력한 만큼 결과가 나와 기쁩니다.', 76.0, 74.0, 72.0, 77.0, 78.0, 6, 76.0, 76.0, 74.0, '학습 성취 단계를 요약 및 정도 어미 구문으로 훌륭히 나타냈습니다.', '2026-04-24 16:05:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '문화재 보호의 당위성', 's3://talktalk/audio/e5_18.wav', 113, 'ELEM_5_6', 'COMPLETED', '2026-04-27 10:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '문화재는 조상의 숨결이 깃든 인류의 자산입니다. 한 번 훼손되면 복구가 불가능합니다.', 77.0, 76.0, 73.0, 78.0, 79.0, 6, 77.0, 77.0, 76.0, '자산, 훼손, 복구 같은 개념어를 사용하여 설득의 타당성을 높임.', '2026-04-27 10:40:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '학교 폭력 예방 캠페인', 's3://talktalk/audio/e5_19.wav', 128, 'ELEM_5_6', 'COMPLETED', '2026-04-30 14:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '사소한 장난도 상대방에게는 깊은 상처를 줄 수 있습니다. 방관하지 말고 도움을 주어야 합니다.', 76.0, 77.0, 74.0, 79.0, 78.0, 6, 78.0, 76.0, 76.0, '방관, 장난의 개념 구도를 활용해 주장-이유 구성을 명확히 함.', '2026-04-30 14:20:00');

-- -----------------------------------------------------------------------
-- 5월 (7개) : 평균 점수대 69~72점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '어린이날의 진정한 의미', 's3://talktalk/audio/e5_20.wav', 119, 'ELEM_5_6', 'COMPLETED', '2026-05-04 09:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '어린이날은 선물만 받는 날이 아니라 인격을 존중받고 행복을 보장받는 날이어야 합니다.', 78.0, 78.0, 76.0, 81.0, 81.0, 6, 79.0, 78.0, 77.0, '인격 존중, 행복 보장 등 인권의 핵심 요소를 유려하게 발화에 담음.', '2026-05-04 09:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '부모님께 감사한 마음', 's3://talktalk/audio/e5_21.wav', 102, 'ELEM_5_6', 'COMPLETED', '2026-05-08 11:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '언제나 저를 믿고 지지해 주시는 부모님 덕분에 제가 건강하게 성장할 수 있었습니다.', 79.0, 79.0, 77.0, 82.0, 83.0, 6, 81.0, 79.0, 78.0, '지지, 성장 같은 관계적 개념 접속이 자연스럽고 차분한 흐름을 유지함.', '2026-05-08 11:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '가족 여행 계획 세우기', 's3://talktalk/audio/e5_22.wav', 95, 'ELEM_5_6', 'COMPLETED', '2026-05-12 16:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '이번 주말에는 경주 불국사에 가서 신라의 숨결을 느끼고 맛있는 쌈밥도 먹고 싶습니다.', 79.0, 78.0, 76.0, 82.0, 81.0, 6, 82.0, 81.0, 79.0, '신라의 숨결이라는 역사적 표현과 개인의 소망이 무리 없이 이어집니다.', '2026-05-12 16:45:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '야외 자전거 안전 장비', 's3://talktalk/audio/e5_23.wav', 114, 'ELEM_5_6', 'COMPLETED', '2026-05-16 13:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '헬멧 착용은 사고 발생 시 충격을 줄여주는 필수 안전장치이므로 철저히 착용해야 합니다.', 81.0, 79.0, 77.0, 83.0, 83.0, 6, 81.0, 82.0, 81.0, '안전 수칙의 논리 흐름 및 끈어 읽기 유창 지표가 매우 뛰어납니다.', '2026-05-16 13:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 역사 바로 알기', 's3://talktalk/audio/e5_24.wav', 130, 'ELEM_5_6', 'COMPLETED', '2026-05-20 10:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '역사를 잊은 민족에게 미래는 없다는 말처럼, 과거의 아픔을 거울삼아 발전해야 합니다.', 82.0, 81.0, 78.0, 84.0, 84.0, 6, 83.0, 83.0, 82.0, '인용 장치 격언을 스피치 서론 구조에 단단하게 정렬해 매칭 시킴.', '2026-05-20 10:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '공공장소 예절 지키기', 's3://talktalk/audio/e5_25.wav', 108, 'ELEM_5_6', 'COMPLETED', '2026-05-24 15:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '대중교통에서 이어폰을 끼고 통화하는 것은 타인에 대한 배려가 부족한 행동입니다.', 81.0, 82.0, 79.0, 86.0, 86.0, 6, 84.0, 83.0, 82.0, '타인 배려, 공공장소 등 사회 규범 판단 어휘 수준이 아주 탁월함.', '2026-05-24 15:50:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 동네 플로깅 후기', 's3://talktalk/audio/e5_26.wav', 141, 'ELEM_5_6', 'COMPLETED', '2026-05-29 11:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '조깅을 하며 쓰레기를 줍는 플로깅을 실천했습니다. 건강과 환경을 동시에 지켜 보람찼습니다.', 83.0, 83.0, 81.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '플로깅 개념의 실천 양식과 결과를 매끄럽게 도입-전개-정리함.', '2026-05-29 11:40:00');

-- -----------------------------------------------------------------------
-- 6월 (5개) : 평균 점수대 72~75점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '625 전쟁과 평화의 소중함', 's3://talktalk/audio/e5_27.wav', 126, 'ELEM_5_6', 'COMPLETED', '2026-06-03 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '호국영령의 희생이 있었기에 오늘날의 평화가 존재합니다. 분단의 아픔을 잊지 맙시다.', 84.0, 84.0, 82.0, 88.0, 89.0, 6, 87.0, 86.0, 84.0, '호국영령, 분단 아픔 같은 민족 사회 담화 용어를 정확하게 소화함.', '2026-06-03 14:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 가꾼 우리 집 미니 정원', 's3://talktalk/audio/e5_28.wav', 110, 'ELEM_5_6', 'COMPLETED', '2026-06-10 14:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '베란다에서 상추와 토마토를 키우고 있습니다. 매일 물을 주며 관찰하니 생명의 신비로움을 느낍니다.', 76.0, 74.0, 72.0, 77.0, 76.0, 6, 74.0, 74.0, 73.0, '생명의 신비로움이라는 깊이 있는 표현이 돋보입니다. 흐름이 아주 자연스러워요.', '2026-06-10 14:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '인터넷 예절과 악성 댓글 방지', 's3://talktalk/audio/e5_29.wav', 125, 'ELEM_5_6', 'COMPLETED', '2026-06-16 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '보이지 않는 공간일수록 상대방을 존중해야 합니다. 악성 댓글은 타인에게 씻을 수 없는 상처를 줍니다.', 77.0, 76.0, 73.0, 78.0, 77.0, 6, 76.0, 76.0, 74.0, '인터넷 공간의 특성과 문제점을 연결하여 설득력 있게 의견을 제시했습니다.', '2026-06-16 11:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름철 물놀이 안전 수칙 발표', 's3://talktalk/audio/e5_30.wav', 105, 'ELEM_5_6', 'COMPLETED', '2026-06-22 15:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '물에 들어가기 전 반드시 준비운동을 해야 경련을 방지할 수 있습니다. 구명조끼 착용은 필수입니다.', 78.0, 78.0, 76.0, 81.0, 79.0, 6, 78.0, 76.0, 77.0, '준비운동과 경련 방지의 인과관계를 고학년 수준에 맞게 정확하게 구사했습니다.', '2026-06-22 15:40:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리가 지켜야 할 공공질서', 's3://talktalk/audio/e5_31.wav', 118, 'ELEM_5_6', 'COMPLETED', '2026-06-28 10:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '공공장소에서 큰 소리로 떠드는 것은 타인에게 피해를 주는 행동입니다. 서로 배려하는 문화가 필요합니다.', 79.0, 78.0, 76.0, 82.0, 81.0, 6, 79.0, 78.0, 79.0, '도입부와 전개부의 연결이 탄탄하며, 배려라는 핵심 가치를 잘 이끌어냈습니다.', '2026-06-28 10:10:00');

-- -----------------------------------------------------------------------
-- 7월 (3개) : 평균 점수대 75~77점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름방학 계획 짜기', 's3://talktalk/audio/e5_32.wav', 122, 'ELEM_5_6', 'COMPLETED', '2026-07-06 14:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '이번 방학에는 부족한 수학 연산을 보충하고 하루에 삼십 분씩 운동을 하여 기초 체력을 기르겠습니다.', 77.0, 76.0, 73.0, 78.0, 77.0, 6, 76.0, 76.0, 74.0, '구체적인 수치와 목적(기초 체력)을 명확하게 연결하여 완결성이 높습니다.', '2026-07-06 14:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우리 역사 속 영웅, 안중근', 's3://talktalk/audio/e5_33.wav', 135, 'ELEM_5_6', 'COMPLETED', '2026-07-15 11:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '안중근 의사는 하얼빈역에서 이토 히로부미를 저격하여 우리 민족의 독립 의지를 전 세계에 알리셨습니다.', 78.0, 76.0, 74.0, 79.0, 78.0, 6, 77.0, 77.0, 76.0, '역사적 사실을 정확한 문장 성분의 호응을 갖추어 차분하게 발표했습니다.', '2026-07-15 11:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '에너지 절약의 실천 방법', 's3://talktalk/audio/e5_34.wav', 114, 'ELEM_5_6', 'COMPLETED', '2026-07-24 16:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '빈 교실의 전등을 끄는 작은 행동만으로도 불필요한 전력 낭비를 막고 환경을 보호할 수 있습니다.', 78.0, 78.0, 76.0, 81.0, 79.0, 6, 78.0, 76.0, 77.0, '전력 낭비라는 개념어 선택이 적절하며, 구체적인 실천 방안이 돋보입니다.', '2026-07-24 16:45:00');

-- -----------------------------------------------------------------------
-- 8월 (8개) : 평균 점수대 77~79점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '박물관 견학 리포트', 's3://talktalk/audio/e5_35.wav', 140, 'ELEM_5_6', 'COMPLETED', '2026-08-03 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '국립중앙박물관에서 반가사유상을 보았습니다. 은은한 미소에서 조상들의 정교한 예술성을 느꼈습니다.', 79.0, 78.0, 76.0, 82.0, 81.0, 6, 79.0, 78.0, 79.0, '정교한 예술성이라는 어휘를 문맥에 알맞게 활용해 스피치의 질을 높였습니다.', '2026-08-03 10:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '독서가 주는 세 가지 선물', 's3://talktalk/audio/e5_36.wav', 150, 'ELEM_5_6', 'COMPLETED', '2026-08-06 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '첫째는 지식이고, 둘째는 간접 경험이며, 셋째는 어휘력입니다. 그러므로 우리는 책을 읽어야 합니다.', 81.0, 79.0, 77.0, 82.0, 83.0, 6, 81.0, 82.0, 81.0, '열거법(첫째, 둘째)을 사용하여 청중이 내용을 체계적으로 이해하기 쉽게 조직했습니다.', '2026-08-06 14:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '여름철 식중독 예방을 위하여', 's3://talktalk/audio/e5_37.wav', 122, 'ELEM_5_6', 'COMPLETED', '2026-08-10 11:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '음식물은 반드시 익혀서 섭취하고 손 씻기를 생활화해야 여름철 배탈과 식중독을 예방할 수 있습니다.', 81.0, 81.0, 78.0, 83.0, 82.0, 6, 82.0, 81.0, 79.0, '생활화, 섭취 등 격식 있는 단어를 사용하여 안정감 있는 리듬으로 발표했습니다.', '2026-08-10 11:00:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '캠핑을 떠났던 추억', 's3://talktalk/audio/e5_38.wav', 131, 'ELEM_5_6', 'COMPLETED', '2026-08-14 20:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '가족들과 산속 캠핑장에서 밤하늘을 보았습니다. 도시에서 보이지 않던 수많은 별들이 쏟아질 듯 맑았습니다.', 82.0, 81.0, 78.0, 84.0, 83.0, 6, 83.0, 81.0, 82.0, '쏟아질 듯 맑았다는 비유적 묘사가 훌륭하며 문장의 흐름이 매우 유려합니다.', '2026-08-14 20:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '내가 도전한 줄넘기 100개', 's3://talktalk/audio/e5_39.wav', 114, 'ELEM_5_6', 'COMPLETED', '2026-08-18 16:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '처음에는 힘들었지만 아침마다 꾸준히 연습했습니다. 드디어 목표를 달성했을 때 큰 성취감을 느꼈습니다.', 82.0, 82.0, 79.0, 84.0, 84.0, 6, 84.0, 82.0, 81.0, '꾸준히와 드디어라는 접속 부사의 배치가 훌륭하며 목적 의식이 돋보입니다.', '2026-08-18 16:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '동물 보호소 봉사활동 소감', 's3://talktalk/audio/e5_40.wav', 145, 'ELEM_5_6', 'COMPLETED', '2026-08-21 13:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '유기견들을 돌보며 인간의 책임감에 대해 생각했습니다. 반려동물을 끝까지 책임지는 태도가 필요합니다.', 83.0, 82.0, 81.0, 86.0, 84.0, 6, 83.0, 83.0, 82.0, '책임감, 유기견 등의 사회적 논점을 본인의 의견과 잘 융합하여 표현했습니다.', '2026-08-21 13:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '기후 변화와 북극곰의 위기', 's3://talktalk/audio/e5_41.wav', 138, 'ELEM_5_6', 'COMPLETED', '2026-08-25 10:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '지구 온난화로 북극의 빙하가 녹아내려 북극곰들이 먹이를 찾지 못해 굶주리고 있습니다. 대책이 시급합니다.', 84.0, 83.0, 81.0, 86.0, 86.0, 6, 84.0, 84.0, 83.0, '빙하가 녹아내려 굶주린다는 인과적 흐름을 고학년답게 설득력 있게 구축함.', '2026-08-25 10:45:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '방학을 마무리하며', 's3://talktalk/audio/e5_42.wav', 120, 'ELEM_5_6', 'COMPLETED', '2026-08-29 15:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '알차게 보낸 방학 덕분에 몸과 마음이 한 뼘 더 자란 것 같습니다. 2학기 학교생활도 기대됩니다.', 84.0, 84.0, 81.0, 87.0, 86.0, 6, 85.0, 83.0, 84.0, '전체적인 스피치의 서론-결론 구조가 매끄럽고 발음 유창성이 우수합니다.', '2026-08-29 15:00:00');

-- -----------------------------------------------------------------------
-- 9월 (1개) : 평균 점수대 79점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '2학기 학급 임원 선거 공약', 's3://talktalk/audio/e5_43.wav', 142, 'ELEM_5_6', 'COMPLETED', '2026-09-10 09:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '말만 앞서는 회장이 아니라, 우리 반의 불편한 점을 직접 듣고 해결하는 발로 뛰는 일꾼이 되겠습니다.', 86.0, 84.0, 81.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '발로 뛴다는 대조 비유 기법을 서론 공약에 알맞게 사용하여 청중을 흡입함.', '2026-09-10 09:20:00');

-- -----------------------------------------------------------------------
-- 10월 (1개) : 평균 점수대 81점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '한글날과 세종대왕의 애민정신', 's3://talktalk/audio/e5_44.wav', 130, 'ELEM_5_6', 'COMPLETED', '2026-10-15 11:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '백성을 사랑하는 애민정신에서 시작된 한글은 과학적이고 독창적인 우리 민족 최고의 문화유산입니다.', 86.0, 84.0, 82.0, 88.0, 88.0, 6, 86.0, 85.0, 84.0, '애민정신, 문화유산 등의 개념 단어 호응이 유려하며 마무리가 훌륭합니다.', '2026-10-15 11:40:00');

-- -----------------------------------------------------------------------
-- 11월 (3개) : 평균 점수대 81~84점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '아나바다 운동과 자원 순환', 's3://talktalk/audio/e5_45.wav', 138, 'ELEM_5_6', 'COMPLETED', '2026-11-05 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '나에게 필요 없는 물건이 타인에게는 유용할 수 있습니다. 자원 순환은 환경 오염을 막는 지름길입니다.', 87.0, 86.0, 82.0, 89.0, 87.0, 6, 86.0, 86.0, 84.0, '자원 순환과 오염 방지의 상관관계를 논리적으로 아주 예쁘게 묶어냈습니다.', '2026-11-05 14:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '사이버 언어폭력의 심각성', 's3://talktalk/audio/e5_46.wav', 148, 'ELEM_5_6', 'COMPLETED', '2026-11-15 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '단톡방에서의 비방과 따돌림은 눈에 보이지 않는 심각한 폭력입니다. 상처 주는 언행을 멈추어야 합니다.', 88.0, 86.0, 83.0, 91.0, 89.0, 6, 87.0, 86.0, 85.0, '비방, 언행 등 한자 도구어를 적절하게 사용하여 주장의 타당성을 단단히 함.', '2026-11-15 10:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '인공지능 로봇과 동행하는 삶', 's3://talktalk/audio/e5_47.wav', 142, 'ELEM_5_6', 'COMPLETED', '2026-11-25 16:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '로봇 기술은 우리 삶을 편리하게 만들지만 인간의 일자리를 대체하는 양날의 검이 될 수도 있습니다.', 88.0, 87.0, 83.0, 91.0, 91.0, 6, 89.0, 88.0, 86.0, '양날의 검이라는 관용 표현을 고학년 담화 맥락에 완벽히 정렬해 매칭 시킴.', '2026-11-25 16:20:00');

-- -----------------------------------------------------------------------
-- 12월 (1개) : 최종 정점 평균 84~87점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '인공지능 발전과 미래 사회의 변화', 's3://talktalk/audio/e5_48.wav', 155, 'ELEM_5_6', 'COMPLETED', '2026-12-20 16:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '인공지능은 인류의 편의를 증진시키지만 기술 소외 계층을 만드는 맹점도 지니고 있습니다. 기술의 발전에 발맞추어 제도적 보안책 마련과 인간 중심의 윤리 의식이 병행되어야 합니다.', 89.0, 87.0, 83.0, 92.0, 91.0, 6, 89.0, 88.0, 87.0, '증진, 맹점, 윤리 의식 병행 등 고학년 성취기준을 완벽하게 뛰어넘는 타당한 논거 조직 능력을 보여준 명작.', '2026-12-20 16:30:00');


-- =======================================================================
-- 중학교 1~2학년 (MIDDLE_1_2) 총 24개 전체 더미 데이터
-- 유저 ID: 6 / 1~4월, 7월, 9~10월 데이터 없음 반영
-- =======================================================================

-- -----------------------------------------------------------------------
-- 5월 (7개) : 점수대 71~74점 (중학 스피치 입문)
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '정보 보안과 비밀번호 관리', 's3://talktalk/audio/m1_01.wav', 121, 'MIDDLE_1_2', 'COMPLETED', '2026-05-04 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '개인정보 유출 예방을 위해 특수문자를 조합한 주기적인 패스워드 변경이 의무화되어야 합니다.', 73.0, 71.0, 64.0, 72.0, 68.0, 6, 71.0, 67.0, 69.0, '중등 사고도구어 매칭이 우수하지만 인과 문장들의 세부 논거 보완이 다소 필요함.', '2026-05-04 10:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '청소년 저작권 의식 강화', 's3://talktalk/audio/m1_02.wav', 135, 'MIDDLE_1_2', 'COMPLETED', '2026-05-08 14:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '불법 다운로드는 창작자의 창의성을 저해하므로 정당한 대가를 지불하는 소비문화 정착이 시급합니다.', 74.0, 72.0, 66.0, 73.0, 71.0, 6, 72.0, 69.0, 71.0, '저해, 정착 등 논조에 알맞은 정확한 용어를 조화롭게 잘 발화했습니다.', '2026-05-08 14:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '동물원 폐지에 대한 찬반', 's3://talktalk/audio/m1_03.wav', 140, 'MIDDLE_1_2', 'COMPLETED', '2026-05-12 11:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '동물권 보호를 위해 전시 목적의 동물원 폐지는 타당하나, 멸종 위기종 보호 기능은 유지되어야 합니다.', 74.0, 73.0, 67.0, 74.0, 72.0, 6, 73.0, 71.0, 72.0, '동물권과 보호 기능의 양면성을 균형 있게 짚어낸 서론 구조가 우수합니다.', '2026-05-12 11:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '디지털 디톡스의 필요성', 's3://talktalk/audio/m1_04.wav', 128, 'MIDDLE_1_2', 'COMPLETED', '2026-05-16 16:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '스마트기기의 알림에서 벗어나 뇌에 휴식을 주는 디지털 디톡스는 현대인의 정신 건강에 필수적입니다.', 76.0, 74.0, 68.0, 76.0, 73.0, 6, 74.0, 72.0, 73.0, '디지털 디톡스라는 핵심 개념을 인과 구조를 통해 알기 쉽게 풀어냈습니다.', '2026-05-16 16:40:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '올바른 언어 사용과 품격', 's3://talktalk/audio/m1_05.wav', 115, 'MIDDLE_1_2', 'COMPLETED', '2026-05-20 09:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '비속어 사용은 일시적 유대감을 줄 수 있으나, 장기적으로는 개인의 품격과 언어 능력을 저하시킵니다.', 76.0, 74.0, 69.0, 77.0, 74.0, 6, 76.0, 73.0, 74.0, '양보절(~수 있으나)을 활용하여 주장의 설득력을 한층 끌어올린 좋은 문장입니다.', '2026-05-20 09:50:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '교내 스마트폰 사용 금지', 's3://talktalk/audio/m1_06.wav', 133, 'MIDDLE_1_2', 'COMPLETED', '2026-05-25 13:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '수업권 보장을 위한 스마트폰 수거는 합당한 조치이나, 학생의 자기 결정권을 침해한다는 비판도 고려해야 합니다.', 77.0, 76.0, 71.0, 78.0, 76.0, 6, 77.0, 74.0, 76.0, '자기 결정권 등 중등 교육과정 수준에 걸맞은 논리어를 아주 잘 활용했습니다.', '2026-05-25 13:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '환경을 위한 채식 식단', 's3://talktalk/audio/m1_07.wav', 145, 'MIDDLE_1_2', 'COMPLETED', '2026-05-29 15:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '축산업에서 발생하는 메탄가스를 줄이기 위해 일주일에 하루는 채식을 실천하는 저탄소 운동에 동참해야 합니다.', 77.0, 76.0, 72.0, 79.0, 76.0, 6, 78.0, 76.0, 77.0, '원인과 해결책이 논리적이고 명확하게 대응하는 훌륭한 문장 구조입니다.', '2026-05-29 15:30:00');

-- -----------------------------------------------------------------------
-- 6월 (5개) : 점수대 74~77점
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '탄소 배출권 거래제 해설', 's3://talktalk/audio/m1_08.wav', 142, 'MIDDLE_1_2', 'COMPLETED', '2026-06-03 10:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '탄소 배출량에 비용을 부과함으로써 기업들의 자발적인 친환경 기술 투자를 유도할 수 있습니다.', 77.0, 76.0, 71.0, 79.0, 76.0, 6, 78.0, 76.0, 74.0, '부과함으로써 같은 구조 도구 연결 조사를 문맥 흐름에 완벽 매칭 시킴.', '2026-06-03 10:00:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '청소년 선거권 연령 하향', 's3://talktalk/audio/m1_09.wav', 150, 'MIDDLE_1_2', 'COMPLETED', '2026-06-08 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '참정권 확대는 청소년의 정치적 주체성을 길러주지만, 포퓰리즘에 휩쓸릴 우려에 대한 교육적 대비가 필요합니다.', 78.0, 77.0, 72.0, 81.0, 78.0, 6, 79.0, 77.0, 76.0, '포퓰리즘, 주체성 같은 고차원적인 개념어를 활용해 쟁점을 명확히 분석했습니다.', '2026-06-08 14:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '촉법소년 연령 하향 논쟁', 's3://talktalk/audio/m1_10.wav', 158, 'MIDDLE_1_2', 'COMPLETED', '2026-06-15 11:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '범죄의 흉포화에 따른 처벌 강화 요구는 타당하나, 교화라는 소년법의 본질적 목적이 훼손되어서는 안 될 것입니다.', 79.0, 78.0, 73.0, 82.0, 79.0, 6, 81.0, 78.0, 77.0, '상반된 두 가지 관점을 대조 접속사로 자연스럽게 연결한 고급 문장입니다.', '2026-06-15 11:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '무인 점포의 윤리적 문제', 's3://talktalk/audio/m1_11.wav', 135, 'MIDDLE_1_2', 'COMPLETED', '2026-06-22 16:05:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '기술 발전이 가져온 편리함 이면에는 보안 취약성으로 인한 잠재적 범죄 유발이라는 사회적 비용이 숨어 있습니다.', 79.0, 79.0, 74.0, 83.0, 81.0, 6, 82.0, 79.0, 78.0, '이면, 잠재적, 사회적 비용 등 사회 현상을 분석하는 어휘 구사력이 탁월함.', '2026-06-22 16:05:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '메타버스 시대의 자아', 's3://talktalk/audio/m1_12.wav', 144, 'MIDDLE_1_2', 'COMPLETED', '2026-06-28 09:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '가상 현실에서의 익명성은 자유로운 표현을 보장하지만, 동시에 현실 자아와의 괴리를 초래할 위험성을 내포합니다.', 81.0, 79.0, 76.0, 84.0, 82.0, 6, 83.0, 81.0, 79.0, '괴리, 내포 등 추상적 어휘를 활용해 현상의 문제점을 정확히 지적했습니다.', '2026-06-28 09:30:00');

-- -----------------------------------------------------------------------
-- 8월 (8개) : 점수대 77~81점 (중등 연령 집중 훈련기)
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '기본소득제 도입의 필요성', 's3://talktalk/audio/m1_13.wav', 160, 'MIDDLE_1_2', 'COMPLETED', '2026-08-04 10:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '노동의 종말이 예견되는 4차 산업혁명 시대에 최소한의 인간다운 삶을 영위하기 위한 기본소득은 시대적 사명입니다.', 81.0, 81.0, 77.0, 84.0, 83.0, 6, 84.0, 82.0, 81.0, '영위, 사명 같은 호소력 짙은 어휘를 적재적소에 배치해 설득력을 극대화함.', '2026-08-04 10:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '유전자 조작 식품의 안전성', 's3://talktalk/audio/m1_14.wav', 148, 'MIDDLE_1_2', 'COMPLETED', '2026-08-08 14:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '식량난 해결이라는 긍정적 측면에도 불구하고, 장기적 섭취가 인체에 미치는 유해성이 입증되지 않았으므로 신중한 접근이 요구됩니다.', 82.0, 82.0, 78.0, 86.0, 84.0, 6, 84.0, 83.0, 82.0, '불구하고, 입증, 신중한 접근 등 논리적 글쓰기 구조를 말하기에 완벽히 이식했습니다.', '2026-08-08 14:30:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '플라스틱 빨대 퇴출 운동', 's3://talktalk/audio/m1_15.wav', 135, 'MIDDLE_1_2', 'COMPLETED', '2026-08-11 11:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '대체재 부족으로 인한 소비자의 불편함은 존재하나, 해양 생태계 보존이라는 거시적 목표를 위해 기꺼이 감수해야 할 불편입니다.', 83.0, 82.0, 79.0, 86.0, 84.0, 6, 86.0, 84.0, 83.0, '거시적 목표와 미시적 불편함을 대조하여 주장의 정당성을 매우 훌륭하게 입증함.', '2026-08-11 11:45:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '대중교통 무료화 정책', 's3://talktalk/audio/m1_16.wav', 142, 'MIDDLE_1_2', 'COMPLETED', '2026-08-15 16:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '대중교통 무료화는 시민의 이동권을 보장하고 대기 오염을 획기적으로 감축할 수 있는 진보적인 복지 정책입니다.', 84.0, 83.0, 79.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '이동권, 감축, 진보적 등 사회 정책을 설명하는 고급 어휘를 무리 없이 다룹니다.', '2026-08-15 16:20:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '학교 폭력 방관자 처벌', 's3://talktalk/audio/m1_17.wav', 156, 'MIDDLE_1_2', 'COMPLETED', '2026-08-20 09:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '물리적 가해뿐만 아니라 암묵적 동조 현상인 방관 역시 피해자에게는 동일한 폭력으로 작용하므로 도의적 책임을 물어야 합니다.', 84.0, 84.0, 81.0, 88.0, 87.0, 6, 87.0, 86.0, 84.0, '암묵적 동조, 도의적 책임 등 추상적 개념을 활용한 인과 관계 연결이 매우 유려합니다.', '2026-08-20 09:15:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '반려동물 보유세 도입 찬성', 's3://talktalk/audio/m1_18.wav', 149, 'MIDDLE_1_2', 'COMPLETED', '2026-08-23 15:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '보유세를 통해 마련된 재원을 유기동물 보호 인프라 확충에 사용한다면 무책임한 파양을 방지하는 실효성 있는 대책이 될 것입니다.', 86.0, 84.0, 82.0, 89.0, 87.0, 6, 88.0, 86.0, 86.0, '재원, 파양, 실효성 등 정책 논증에 필수적인 도구어들을 완벽하게 소화해냈습니다.', '2026-08-23 15:50:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '인공지능 판사의 공정성', 's3://talktalk/audio/m1_19.wav', 165, 'MIDDLE_1_2', 'COMPLETED', '2026-08-27 13:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '과거의 판례 데이터를 학습하는 AI는 기존 사회의 편견을 그대로 답습할 위험이 있으므로 전적인 사법 판단을 위임하는 것은 시기상조입니다.', 86.0, 86.0, 83.0, 89.0, 88.0, 6, 89.0, 87.0, 86.0, '답습, 위임, 시기상조 등의 어휘를 통해 AI의 맹점을 매우 날카롭게 파고들었습니다.', '2026-08-27 13:10:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '역사 교과서 국정화 반대', 's3://talktalk/audio/m1_20.wav', 152, 'MIDDLE_1_2', 'COMPLETED', '2026-08-30 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '역사는 다양한 관점의 교차 검증을 통해 완성되므로, 국가 주도의 단일 교과서는 학생들의 다원적 사고를 저해하는 결과를 낳습니다.', 87.0, 86.0, 84.0, 91.0, 89.0, 6, 89.0, 88.0, 87.0, '교차 검증, 다원적 사고 등 중등 토론 담화의 최고 수준 성취를 보여준 문장입니다.', '2026-08-30 10:30:00');

-- -----------------------------------------------------------------------
-- 11월 (3개) : 점수대 81~84점 (중학 12학년 과정 최종 안착 마스터)
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '사형제도 폐지에 대한 고찰', 's3://talktalk/audio/m1_21.wav', 158, 'MIDDLE_1_2', 'COMPLETED', '2026-11-06 14:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '오판의 가능성을 배제할 수 없는 상황에서 생명권 박탈이라는 비가역적 형벌을 집행하는 것은 국가 폭력의 연장선으로 볼 수 있습니다.', 87.0, 87.0, 84.0, 91.0, 89.0, 6, 91.0, 89.0, 88.0, '비가역적, 연장선 등 철학적 논제를 다루는 표현의 깊이와 유창성이 매우 훌륭함.', '2026-11-06 14:00:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '노키즈존과 영업의 자유', 's3://talktalk/audio/m1_22.wav', 160, 'MIDDLE_1_2', 'COMPLETED', '2026-11-14 11:25:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '특정 연령층 전체를 잠재적 위협으로 규정하여 공간 접근을 원천 차단하는 것은 영업의 자유를 넘어선 명백한 차별 행위입니다.', 88.0, 87.0, 84.0, 92.0, 91.0, 6, 91.0, 89.0, 88.0, '원천 차단, 잠재적 위협 등의 논리적 규정 방식이 토론의 정석을 보여줍니다.', '2026-11-14 11:25:00');

INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '유기동물 안락사 문제', 's3://talktalk/audio/m1_23.wav', 165, 'MIDDLE_1_2', 'COMPLETED', '2026-11-25 16:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '수용 공간의 물리적 한계로 인한 불가피한 조치라는 점은 이해하나, 근본적인 입양 문화 개선 없이 안락사에 의존하는 것은 생명 윤리에 위배됩니다.', 89.0, 88.0, 86.0, 92.0, 92.0, 6, 92.0, 91.0, 89.0, '불가피한 조치, 생명 윤리 위배 등 상반된 논거를 매끄럽게 연결해 낸 완벽한 복문입니다.', '2026-11-25 16:10:00');

-- -----------------------------------------------------------------------
-- 12월 (1개) : 최종 정점 84~86점 (MIDDLE_1_2 최종 성장작)
-- -----------------------------------------------------------------------
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '확증 편향과 알고리즘 맹신 오류 비판', 's3://talktalk/audio/m1_24.wav', 162, 'MIDDLE_1_2', 'COMPLETED', '2026-12-18 15:45:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '알고리즘이 고착화시킨 정보 필터 버블은 사용자의 편향된 시각을 심화시킵니다. 따라서 다각적인 관점을 수용하는 주체적 미디어 리터러시 역량 함양이 무엇보다 필수적입니다.', 88.0, 86.0, 82.0, 91.0, 89.0, 6, 87.0, 86.0, 85.0, '고착화, 편향, 함양 등 중등 핵심 필수 도구어를 바탕으로 한 도입-전개-정리 구조의 완결도가 최고 수준에 달함.', '2026-12-18 15:45:00');


-- -----------------------------------------------------------------------
-- [5] 중학교 3학년 (MIDDLE_3) - 총 18개 데이터
-- 기획: 1~6월 각 달 3개씩 고정 배치, 7~12월 0개
-- -----------------------------------------------------------------------

-- 1월 (3개) : 점수대 66~69점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '고교 학점제 찬반 논증 1', 's3://talktalk/audio/m3_01.wav', 125, 'MIDDLE_3', 'COMPLETED', '2026-01-07 10:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '고교 학점제는 선택권을 넓히지만 인프라 격차 유발 오류가 우려되는 면이 존재합니다.', 71.0, 68.0, 62.0, 72.0, 67.0, 6, 69.0, 66.0, 68.0, '논점이 참신하나 중3 수준의 객관적 데이터 근거 보완이 요구됩니다.', '2026-01-07 10:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '청소년 노동 인권 보호', 's3://talktalk/audio/m3_02.wav', 132, 'MIDDLE_3', 'COMPLETED', '2026-01-16 14:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '단순 아르바이트생이라도 근로기준법상 정당한 권익과 임금을 보장받는 것은 헌법적 가치입니다.', 72.0, 69.0, 64.0, 73.0, 68.0, 6, 71.0, 67.0, 69.0, '법학 용어 구사력은 우수하나 전개 결론 단락 연계 구조를 다듬어 보세요.', '2026-01-16 14:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '대중문화 소비의 맹점', 's3://talktalk/audio/m3_03.wav', 140, 'MIDDLE_3', 'COMPLETED', '2026-01-27 16:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '미디어 상업주의는 획일화된 미적 기준을 강요하여 청소년 자아정체성에 왜곡을 초래합니다.', 73.0, 71.0, 64.0, 74.0, 69.0, 6, 72.0, 69.0, 71.0, '왜곡 초래 등 개념도구어를 알맞은 호응 배치로 영리하게 구사했습니다.', '2026-01-27 16:00:00');

-- 2월 (3개) : 점수대 69~72점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '전통문화 계승 방안', 's3://talktalk/audio/m3_04.wav', 115, 'MIDDLE_3', 'COMPLETED', '2026-02-04 11:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '박제된 전통이 아닌 현대적 콘텐츠 결합을 통한 실질적 융합만이 계승의 대안입니다.', 74.0, 72.0, 66.0, 76.0, 71.0, 6, 74.0, 71.0, 72.0, '융합, 계승 등 한자 추상어를 맥락에 결함 없이 훌륭히 안착시켰습니다.', '2026-02-04 11:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '기본소득 도입의 타당성', 's3://talktalk/audio/m3_05.wav', 148, 'MIDDLE_3', 'COMPLETED', '2026-02-15 15:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '자동화로 인한 고용 절벽 시대의 사회안전망으로서 보편적 기본소득은 선제적 필수 조건입니다.', 76.0, 73.0, 68.0, 77.0, 72.0, 6, 76.0, 72.0, 73.0, '조건 인과 논증 구조 설계가 중3 평균 지표에 정확하게 수렴합니다.', '2026-02-15 15:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '동물실험의 윤리적 쟁점', 's3://talktalk/audio/m3_06.wav', 135, 'MIDDLE_3', 'COMPLETED', '2026-02-23 10:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '인간의 이익을 위한 생명권 침해는 정당화될 수 없으며 대체 연구 강화를 적극 도모해야 합니다.', 76.0, 74.0, 69.0, 78.0, 73.0, 6, 77.0, 74.0, 74.0, '당위 표현(~해야 합니다)의 유기적 선후 배치가 안정감 있게 수행됨.', '2026-02-23 10:10:00');

-- 3월 (3개) : 점수대 72~75점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '미디어 리터러시 역량', 's3://talktalk/audio/m3_07.wav', 152, 'MIDDLE_3', 'COMPLETED', '2026-03-05 09:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '정보 과잉 시대 속 편향된 수용을 지양하고 가짜 뉴스를 필터링하는 주체적 리터러시가 시급합니다.', 77.0, 76.0, 71.0, 79.0, 76.0, 6, 78.0, 76.0, 74.0, '지양하다 라는 용어를 문맥의 어조에 완벽하게 결합해 발화했습니다.', '2026-03-05 09:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우주 개발과 자원 경쟁', 's3://talktalk/audio/m3_08.wav', 128, 'MIDDLE_3', 'COMPLETED', '2026-03-17 14:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '우주 영토 확장은 단순 호기심을 넘어 미래 자원 패권 경쟁의 심장부로 부상하고 있습니다.', 78.0, 76.0, 72.0, 81.0, 77.0, 6, 79.0, 77.0, 76.0, '패권 경쟁 등 사회적 담화용 어휘가 조화롭게 배치되어 완성도가 올라감.', '2026-03-17 14:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '노키즈존 논란과 인권', 's3://talktalk/audio/m3_09.wav', 161, 'MIDDLE_3', 'COMPLETED', '2026-03-26 11:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '영업의 자유도 중요하지만 특정 집단 배제는 차별적 관행을 정당화하는 위험한 선례입니다.', 79.0, 78.0, 73.0, 82.0, 78.0, 6, 81.0, 78.0, 77.0, '서론에서 쟁점 제시를 유기적 구조로 명확히 명시한 점이 일품입니다.', '2026-03-26 11:10:00');

-- 4월 (3개) : 점수대 75~78점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '플라스틱 규제와 환경', 's3://talktalk/audio/m3_10.wav', 144, 'MIDDLE_3', 'COMPLETED', '2026-04-03 15:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '미세 플라스틱의 내부 축적은 먹이사슬을 타고 결국 인간 건강의 위협으로 귀결될 것입니다.', 81.0, 78.0, 74.0, 83.0, 81.0, 6, 82.0, 79.0, 78.0, '귀결되다 등의 인과론적 용어 선택 및 종결어미 조화가 탁월합니다.', '2026-04-03 15:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '공유 경제의 명과 암', 's3://talktalk/audio/m3_11.wav', 130, 'MIDDLE_3', 'COMPLETED', '2026-04-14 10:40:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '자원의 효율적 이용이라는 장점 뒤에 플랫폼 노동자의 불안정한 고용 구조라는 그늘이 있습니다.', 82.0, 79.0, 76.0, 84.0, 83.0, 6, 83.0, 81.0, 79.0, '명과 암의 상반 구도를 전환 부사어 배치로 탄탄하게 대조해 냄.', '2026-04-14 10:40:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '역사 왜곡 대응 방안', 's3://talktalk/audio/m3_12.wav', 155, 'MIDDLE_3', 'COMPLETED', '2026-04-22 13:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '감정적 대응을 지양하고 객관적 고증 자료를 바탕으로 한 국제 사회적 연대가 핵심 솔루션입니다.', 83.0, 81.0, 77.0, 84.0, 84.0, 6, 84.0, 82.0, 81.0, '솔루션에 부합하는 정교한 단락 유기성이 복문 구조 속에 안착함.', '2026-04-22 13:50:00');

-- 5월 (3개) : 점수대 78~81점
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '지속 가능한 패션 비전', 's3://talktalk/audio/m3_13.wav', 138, 'MIDDLE_3', 'COMPLETED', '2026-05-06 11:10:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '패스트 패션이 야기하는 유행 파괴는 환경 파괴의 주범이므로 업사이클링 전환이 시급히 요구됩니다.', 84.0, 82.0, 78.0, 86.0, 84.0, 6, 85.0, 83.0, 82.0, '주범이므로 라는 인과 조사의 결합 호응이 문장 속에서 매우 우수함.', '2026-05-06 11:10:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '지방 소멸과 국가 균형', 's3://talktalk/audio/m3_14.wav', 165, 'MIDDLE_3', 'COMPLETED', '2026-05-18 16:30:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '수도권 과밀화는 지방 소멸을 가속화하므로 지역 거점 대학과 인프라의 파격적 투자가 수반되어야 합니다.', 84.0, 83.0, 79.0, 87.0, 86.0, 6, 86.0, 84.0, 84.0, '가속화하므로, 수반되어야 같은 사회논리어 연결 구사력이 최상급임.', '2026-05-18 16:30:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '소비자 주권과 불매운동', 's3://talktalk/audio/m3_15.wav', 142, 'MIDDLE_3', 'COMPLETED', '2026-05-27 14:00:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '기업의 반사회적 경영 행태에 경종을 울리는 불매운동은 정당한 소비자 주권 행사의 일환입니다.', 86.0, 84.0, 81.0, 88.0, 87.0, 6, 86.0, 84.0, 83.0, '경종을 울리는 은유적 관용구 장치가 스피치의 청중 집중도를 유도함.', '2026-05-27 14:00:00');

-- 6월 (3개) : 점수대 81~86점 (MIDDLE_3 최종 정점 발화 리포트)
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '원자력 발전 확대 논쟁 찬반', 's3://talktalk/audio/m3_16.wav', 170, 'MIDDLE_3', 'COMPLETED', '2026-06-03 10:15:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '탄소 중립 달성을 위한 원전 확대 주장은 타당하나 폐기물 처리장 확보라는 근본적 안전장치가 배제되어서는 안 됩니다.', 86.0, 86.0, 81.0, 88.0, 88.0, 6, 87.0, 86.0, 85.0, '쟁점 인용과 반론 양보 수용 절차가 유기적으로 완벽히 맞물린 문장임.', '2026-06-03 10:15:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '우주 쓰레기 규제 법안', 's3://talktalk/audio/m3_17.wav', 158, 'MIDDLE_3', 'COMPLETED', '2026-06-14 15:20:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '궤도를 도는 우주 쓰레기는 인류 자산인 인공위성을 파괴할 시한폭탄이므로 국제법적 구속력 제정이 급선무입니다.', 87.0, 86.0, 83.0, 89.0, 88.0, 6, 88.0, 87.0, 86.0, '시한폭탄, 급선무 같은 논증 어휘를 활용해 설득 곡선을 꼼꼼히 설계함.', '2026-06-14 15:20:00');
INSERT INTO speeches (user_id, title, audio_url, duration, target_level, status, created_at) VALUES (6, '안락사 제한적 허용 찬반 논증', 's3://talktalk/audio/m3_18.wav', 175, 'MIDDLE_3', 'COMPLETED', '2026-06-28 16:50:00');
INSERT INTO speech_analysis (speech_id, transcript, accuracy_score, fluency_score, completeness_score, prosody_score, vocabulary_score, word_count, logic_score, sentence_score, structure_score, overall_feedback, created_at) VALUES (LAST_INSERT_ID(), '물론 생명 경시 풍조라는 반론의 타당성도 인정합니다. 그러나 환자의 자기 결정권과 고통 경감이라는 측면을 종합적으로 고려할 때 제도적 보완을 전제로 한 제한적 허용이 결론적으로 합리적입니다.', 89.0, 88.0, 84.0, 91.0, 92.0, 6, 89.0, 89.0, 87.0, '반론 수용 후 전환 접속어를 활용한 재반론까지 중3 심화 담화 성취 기준을 압도적으로 만족한 정점 발화 작품.', '2026-06-28 16:50:00');

-- 7월 ~ 12월 (0개) - 공백 조건 통과