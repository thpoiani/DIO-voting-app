package me.dio.domain;

import java.util.List;

public record Election(String id, List<Candidate> candidates) {
}
